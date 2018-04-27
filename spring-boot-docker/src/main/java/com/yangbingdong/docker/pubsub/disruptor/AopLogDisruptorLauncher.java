package com.yangbingdong.docker.pubsub.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEvent;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEventFactory;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogTranslator;
import com.yangbingdong.docker.pubsub.disruptor.handler.AbstractShardingHandler;
import com.yangbingdong.docker.pubsub.disruptor.handler.CleanHandler;
import com.yangbingdong.docker.pubsub.disruptor.handler.DisruptorHandler;
import com.yangbingdong.springboot.common.utils.CollectionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.yangbingdong.springboot.common.utils.StringUtil.lowercaseInitial;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AopLogDisruptorLauncher implements InitializingBean, ApplicationListener<ContextClosedEvent>,
		DisruptorLauncher<AccessLog> {
	public static final AtomicInteger P_ATOMIC_INTEGER = new AtomicInteger(0);
	private final AopLogEventFactory eventFactory;
	private final AopLogTranslator translator;
	private final DisruptorExceptionHandler exceptionHandler;
	private final GenericWebApplicationContext context;

	@Resource
	private List<DisruptorHandler<AopLogEvent>> disruptorHandlers = new ArrayList<>(16);

	private Disruptor<AopLogEvent> disruptor;
	private RingBuffer<AopLogEvent> ringBuffer;

	@Value("${buffer-size-power:20}")
	private int bufferSizePower;

	@Override
	public void afterPropertiesSet() {
		log.info("Disruptor<{}> initialing......", AopLogEvent.class.getSimpleName());

		BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("disruptor-thread-%d")
																		   .daemon(true)
																		   .build();
		int bufferSize = 1 << bufferSizePower;

		disruptor = new Disruptor<>(eventFactory, bufferSize, threadFactory, ProducerType.MULTI, new BlockingWaitStrategy());
		disruptor.setDefaultExceptionHandler(exceptionHandler);

		if (CollectionUtil.isEmpty(disruptorHandlers)) {
			throw new IllegalArgumentException("Can not found handler!");
		}
		resolveDisruptorHandlers();
		disruptor.start();
		ringBuffer = disruptor.getRingBuffer();

		log.info("Disruptor<{}> initialed......", AopLogEvent.class.getSimpleName());
	}

	@SuppressWarnings("unchecked")
	private void resolveDisruptorHandlers() {
		disruptorHandlers = disruptorHandlers.stream()
											 .sorted(comparingInt(DisruptorHandler::horizontalOrder))
											 .collect(toList());
		int size = disruptorHandlers.size();
		EventHandlerGroup<AopLogEvent> handlerGroup = null;
		DisruptorHandler<AopLogEvent>[] disruptorHandlerArray;
		for (int i = 0; i < size; i++) {
			DisruptorHandler<AopLogEvent> handler = disruptorHandlers.get(i);
			if (handler instanceof AbstractShardingHandler) {
				int shardingQuantity = ((AbstractShardingHandler<AopLogEvent>) handler).addShardingQuantity().getShardingQuantity();
				disruptorHandlerArray = new DisruptorHandler[shardingQuantity];
				disruptorHandlerArray[0] = handler;
				for (int j = 1; j < shardingQuantity; j++) {
					String beanName = lowercaseInitial(handler.getClass().getSimpleName()) + "-" + j;
					context.registerBean(beanName, handler.getClass());
					AbstractShardingHandler<AopLogEvent> shardingBean = (AbstractShardingHandler<AopLogEvent>) context.getBean(beanName);
					shardingBean.setCurrentShard(j);
					shardingBean.setShardingQuantity(shardingQuantity);
					disruptorHandlerArray[j] = shardingBean;
				}
			}else {
				disruptorHandlerArray = singletonList(handler).toArray(new DisruptorHandler[1]);
			}
			if (i == 0) {
				handlerGroup = disruptor.handleEventsWith(disruptorHandlerArray);
			}else {
				handlerGroup = handlerGroup.then(disruptorHandlerArray);
			}
		}
		if (handlerGroup == null) {
			throw new NullPointerException();
		}
		handlerGroup.then(new CleanHandler());
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.info("Destroying Disruptor<{}>", AopLogEvent.class.getSimpleName());
		try {
			disruptor.shutdown(10, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			log.error("Disruptor shutdown error!", e);
		}
	}

	@Override
	public void launch(AccessLog accessLog) {
		P_ATOMIC_INTEGER.incrementAndGet();
		ringBuffer.publishEvent(translator, accessLog);
	}
}
