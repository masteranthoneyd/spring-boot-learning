package com.yangbingdong.docker.pubsub.disruptor.core;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextClosedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yangbingdong.springboot.common.utils.CollectionUtil.isEmpty;
import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public abstract class AbstractDisruptorPublisher<S, E extends DisruptorEvent<S>> implements DisruptorPublisher<S> {
	private DefaultDisruptorCommonComponents components = new DefaultDisruptorCommonComponents();
	private ExceptionHandler<E> exceptionHandler = new DefaultExceptionHandler<>();
	private Disruptor<E> disruptor;
	private RingBuffer<E> ringBuffer;

	private EventTranslatorOneArg<E, S> translatorOneArg;

	@Override
	public void afterPropertiesSet() {
		log.info("Disruptor<{}> initialing......", this.getClass().getSimpleName());
		customDisruptorComponents(components);
		int bufferSize = 1 << components.getBufferSizePower();
		disruptor = new Disruptor<>(provideEventFactory(), bufferSize,
				components.getThreadFactory(), components.getProducerType(), components.getWaitStrategy());
		disruptor.setDefaultExceptionHandler(exceptionHandler);

		List<DisruptorEventHandler<E>> disruptorHandlers = provideDisruptorEventHandlers();
		if (isEmpty(disruptorHandlers)) {
			throw new IllegalArgumentException("Can not found handler!");
		}
		Map<Integer, List<DisruptorEventHandler<E>>> map = groupByOrder(disruptorHandlers);
		List<Integer> keyList = resolveSortKeyList(map);
		resolveDisruptorHandlers(disruptor, map, keyList);
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();
		injectTranslatorOneArg();
		log.info("Disruptor<{}> initialed......", this.getClass().getSimpleName());
	}

	private List<Integer> resolveSortKeyList(Map<Integer, List<DisruptorEventHandler<E>>> map) {
		List<Integer> keyList = new ArrayList<>(map.size());
		keyList.addAll(map.keySet());
		keyList.sort(comparing(identity()));
		return keyList;
	}

	private Map<Integer, List<DisruptorEventHandler<E>>> groupByOrder(List<DisruptorEventHandler<E>> disruptorHandlers) {
		return disruptorHandlers.stream()
								.collect(groupingBy(DisruptorEventHandler::order));
	}

	private void injectTranslatorOneArg() {
		this.translatorOneArg = provideTranslatorOneArg();
	}

	@SuppressWarnings("unchecked")
	private void resolveDisruptorHandlers(Disruptor<E> disruptor, Map<Integer, List<DisruptorEventHandler<E>>> map, List<Integer> keyList) {
		EventHandlerGroup<E> handlerGroup = null;
		EventHandler<E> eventHandler;
		List<EventHandler<E>> verticalEventHandlers;
		int size = keyList.size();
		for (int i = 0; i < size; i++) {
			verticalEventHandlers = new ArrayList<>(16);
			List<DisruptorEventHandler<E>> disruptorEventHandlers = map.get(keyList.get(i));
			if (isEmpty(disruptorEventHandlers)) {
				throw new IllegalArgumentException();
			}
			for (DisruptorEventHandler<E> handler : disruptorEventHandlers) {
				if (handler.enableSharding()) {
					int shardingQuantity = handler.shardingQuantity();
					for (int j = 0; j < shardingQuantity; j++) {
						eventHandler = buildShardingHandler(shardingQuantity, j, handler);
						verticalEventHandlers.add(eventHandler);
					}
				} else {
					eventHandler = (event, sequence, endOfBatch) -> handler.onEvent(event, sequence, endOfBatch, 0);
					verticalEventHandlers.add(eventHandler);
				}
			}
			if (isEmpty(verticalEventHandlers)) {
				throw new IllegalArgumentException();
			}
			if (i == 0) {
				handlerGroup = disruptor.handleEventsWith(verticalEventHandlers.toArray(new EventHandler[0]));
			} else {
				handlerGroup = handlerGroup.then(verticalEventHandlers.toArray(new EventHandler[0]));
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

	protected abstract void customDisruptorComponents(DefaultDisruptorCommonComponents components);

	protected abstract EventFactory<E> provideEventFactory();

	protected abstract List<DisruptorEventHandler<E>> provideDisruptorEventHandlers();

	protected abstract EventTranslatorOneArg<E, S> provideTranslatorOneArg();
}
