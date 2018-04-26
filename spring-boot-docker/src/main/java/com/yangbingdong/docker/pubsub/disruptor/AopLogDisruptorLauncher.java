package com.yangbingdong.docker.pubsub.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEvent;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEventFactory;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogTranslator;
import com.yangbingdong.docker.pubsub.disruptor.handler.AopLogEventPersistHandler;
import com.yangbingdong.docker.pubsub.disruptor.handler.CleanHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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
	private final AopLogEventFactory eventFactory;
	private final AopLogTranslator translator;
	private final DisruptorExceptionHandler exceptionHandler;

	private final AopLogEventPersistHandler persistHandler;
	private final CleanHandler cleanHandler;

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

		disruptor = new Disruptor<>(eventFactory, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());
		disruptor.setDefaultExceptionHandler(exceptionHandler);
		disruptor.handleEventsWith(persistHandler)
				 .then(cleanHandler);
		disruptor.start();

		ringBuffer = disruptor.getRingBuffer();

		log.info("Disruptor<{}> initialed......", AopLogEvent.class.getSimpleName());
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.info("Destroying Disruptor<{}>", AopLogEvent.class.getSimpleName());
		if (event.getApplicationContext().getParent() == null) {
			try {
				disruptor.shutdown(5, TimeUnit.SECONDS);
			} catch (TimeoutException e) {
				log.error("Disruptor shutdown error!", e);
			}
		}
	}

	@Override
	public void launch(AccessLog accessLog) {
		ringBuffer.publishEvent(translator, accessLog);
	}
}
