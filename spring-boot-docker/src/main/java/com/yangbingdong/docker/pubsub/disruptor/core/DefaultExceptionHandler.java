package com.yangbingdong.docker.pubsub.disruptor.core;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class DefaultExceptionHandler<E> implements ExceptionHandler<E> {
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
