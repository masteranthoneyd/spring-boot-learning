package com.yangbingdong.springboot.common.utils.disruptor;

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
	private ExceptionHandler<E> exceptionHandler = new DefaultExceptionHandler<>();
	private Disruptor<E> disruptor;
	private RingBuffer<E> ringBuffer;
	private EventTranslatorOneArg<E, S> translatorOneArg;
	private String eventName;

	@Override
	public void afterPropertiesSet() {
		eventName = provideEventType().getSimpleName();
		log.info("Disruptor<{}> initialing......", eventName);
		DefaultDisruptorCommonComponents components = provideDisruptorInitialComponents();
		if (components == null) {
			log.warn("DisruptorCommonComponents is null,DefaultDisruptorCommonComponents will be use");
			components = new DefaultDisruptorCommonComponents();
		}
		disruptor = newDisruptor(components);
		disruptor.setDefaultExceptionHandler(exceptionHandler);
		EventHandlerGroup<E> handlerGroup = resolveDisruptorHandlers(disruptor);
		if (handlerGroup == null) {
			throw new NullPointerException();
		}
		handlerGroup.then(new FinalCleanHandler<>());
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();
		injectTranslatorOneArg();
		log.info("Disruptor<{}> initialed......", eventName);
	}

	private Disruptor<E> newDisruptor(DefaultDisruptorCommonComponents components) {
		int bufferSize = 1 << components.getBufferSizePower();
		return new Disruptor<>(provideEventFactory(), bufferSize,
				components.getThreadFactory(), components.getProducerType(), components.getWaitStrategy());
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
	private EventHandlerGroup<E> resolveDisruptorHandlers(Disruptor<E> disruptor) {
		List<DisruptorEventHandler<E>> disruptorHandlers = provideDisruptorEventHandlers();
		if (isEmpty(disruptorHandlers)) {
			throw new IllegalArgumentException("Can not found handler!");
		}
		Map<Integer, List<DisruptorEventHandler<E>>> map = groupByOrder(disruptorHandlers);
		List<Integer> keyList = resolveSortKeyList(map);
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
				if (handler.getClass().isAnnotationPresent(Sharding.class)) {
					Sharding sharding = handler.getClass().getAnnotation(Sharding.class);
					int shardingQuantity = sharding.value();
					if (shardingQuantity < 1) {
						throw new IllegalArgumentException();
					}
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
		return handlerGroup;
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
		log.info("Disruptor<{}> shutdown", eventName);
		if (disruptor != null) {
			try {
				disruptor.shutdown(5, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				log.error("Disruptor shutdown error!", e);
			}
		}
	}

	protected abstract DefaultDisruptorCommonComponents provideDisruptorInitialComponents();

	protected abstract EventFactory<E> provideEventFactory();

	protected abstract List<DisruptorEventHandler<E>> provideDisruptorEventHandlers();

	protected abstract EventTranslatorOneArg<E, S> provideTranslatorOneArg();

	protected abstract Class<E> provideEventType();
}
