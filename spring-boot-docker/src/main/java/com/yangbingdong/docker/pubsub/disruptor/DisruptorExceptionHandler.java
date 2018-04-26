package com.yangbingdong.docker.pubsub.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
@Slf4j
public class DisruptorExceptionHandler implements ExceptionHandler<AopLogEvent> {
	@Override
	public void handleEventException(Throwable ex, long sequence, AopLogEvent event) {
		log.error("AopLogEvent handler error", ex);
	}

	@Override
	public void handleOnStartException(Throwable ex) {
		log.error("AopLogEvent handler error", ex);
	}

	@Override
	public void handleOnShutdownException(Throwable ex) {
		log.error("AopLogEvent handler error", ex);
	}
}
