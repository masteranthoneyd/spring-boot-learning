package com.yangbingdong.docker.pubsub.disruptor.core;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.springboot.common.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public abstract class AbstractDisruptorPublisher<S, E extends AbstractDisruptorEvent<S>> implements DisruptorPublisher<S> {
	private int bufferSizePower = 18;
	private ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("disruptor-%d")
																		  .daemon(true)
																		  .build();
	private ProducerType multi = ProducerType.MULTI;
	private WaitStrategy waitStrategy = new BlockingWaitStrategy();
	private ExceptionHandler<E> exceptionHandler = new DefaultExceptionHandler<>();
	private Disruptor<E> disruptor;
	private RingBuffer<E> ringBuffer;

	private EventTranslatorOneArg<E, S> translatorOneArg;

	@Override
	public void afterPropertiesSet() {
		log.info("Disruptor<{}> initialing......", this.getClass().getSimpleName());
		int bufferSize = 1 << bufferSizePower;
		disruptor = new Disruptor<>(provideEventFactory(), bufferSize, threadFactory, multi, waitStrategy);
		disruptor.setDefaultExceptionHandler(exceptionHandler);

		List<DisruptorEventHandler<E>> disruptorHandlers = provideDisruptorEventHandlers();
		if (CollectionUtil.isEmpty(disruptorHandlers)) {
			throw new IllegalArgumentException("Can not found handler!");
		}
		disruptorHandlers = sortHandlers(disruptorHandlers);
		resolveDisruptorHandlers(disruptor, disruptorHandlers);
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();
		setTranslatorOneArg();
		log.info("Disruptor<{}> initialed......", this.getClass().getSimpleName());
	}

	private void setTranslatorOneArg() {
		this.translatorOneArg = provideTranslatorOneArg();
	}

	private List<DisruptorEventHandler<E>> sortHandlers(List<DisruptorEventHandler<E>> disruptorHandlers) {
		return disruptorHandlers.stream()
								.sorted(comparingInt(DisruptorEventHandler::order))
								.collect(toList());
	}

	@SuppressWarnings("unchecked")
	private void resolveDisruptorHandlers(Disruptor<E> disruptor, List<DisruptorEventHandler<E>> disruptorHandlers) {
		int size = disruptorHandlers.size();
		EventHandlerGroup<E> handlerGroup = null;
		EventHandler<E>[] eventHandlerArray;
		EventHandler<E> eventHandler;
		for (int i = 0; i < size; i++) {
			DisruptorEventHandler<E> handler = disruptorHandlers.get(i);
			if (handler.enableSharding()) {
				int shardingQuantity = handler.shardingQuantity();
				eventHandlerArray = new EventHandler[shardingQuantity];
				for (int j = 0; j < shardingQuantity; j++) {
					eventHandler = buildShardingHandler(shardingQuantity, j, handler);
					eventHandlerArray[j] = eventHandler;
				}
			} else {
				eventHandler = (event, sequence, endOfBatch) -> handler.onEvent(event, sequence, endOfBatch, 0);
				eventHandlerArray = singletonList(eventHandler).toArray(new EventHandler[1]);
			}
			if (i == 0) {
				handlerGroup = disruptor.handleEventsWith(eventHandlerArray);
			} else {
				handlerGroup = handlerGroup.then(eventHandlerArray);
			}
		}

		if (handlerGroup == null) {
			throw new NullPointerException();
		}
		handlerGroup.then((EventHandler<E>) (event, sequence, endOfBatch) -> event.clean());
	}

	private EventHandler<E> buildShardingHandler(int shardingQuantity, int currentShard, DisruptorEventHandler<E> handler) {
		return (event, sequence, endOfBatch) -> {
			if (sequence % shardingQuantity == currentShard) {
				handler.onEvent(event, sequence, endOfBatch, currentShard);
			}
		};
	}

	@Override
	public void publish(S source) {
		ringBuffer.publishEvent(translatorOneArg, source);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		if (disruptor != null) {
			try {
				disruptor.shutdown(5, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				log.error("Disruptor shutdown error!", e);
			}
		}
	}

	public void setBufferSizePower(int bufferSizePower) {
		this.bufferSizePower = bufferSizePower;
	}

	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = threadFactory;
	}

	public void setMulti(ProducerType multi) {
		this.multi = multi;
	}

	public void setWaitStrategy(WaitStrategy waitStrategy) {
		this.waitStrategy = waitStrategy;
	}


	private static class DefaultExceptionHandler<E> implements ExceptionHandler<E> {

		@Override
		public void handleEventException(Throwable ex, long sequence, E event) {
			log.error(event.getClass().getSimpleName() + " handler error", ex);
		}

		@Override
		public void handleOnStartException(Throwable ex) {
			log.error("handler start error", ex);
		}

		@Override
		public void handleOnShutdownException(Throwable ex) {
			log.error("handler shutdown error", ex);
		}
	}

	protected abstract EventFactory<E> provideEventFactory();

	protected abstract List<DisruptorEventHandler<E>> provideDisruptorEventHandlers();

	protected abstract EventTranslatorOneArg<E,S> provideTranslatorOneArg();
}
