package com.yangbingdong.springboot.common.config.shutdown;

import io.undertow.server.handlers.GracefulShutdownHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author ybd
 * @date 18-4-19
 * @contact yangbingdong1994@gmail.com
 */
@RequiredArgsConstructor
@Slf4j
public class GracefulShutdownListener implements ApplicationListener<ContextClosedEvent> {

	private final GracefulShutdownWrapper gracefulShutdownWrapper;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		GracefulShutdownHandler gracefulShutdownHandler = gracefulShutdownWrapper.getGracefulShutdownHandler();
		try {
			gracefulShutdownHandler.shutdown();
			gracefulShutdownHandler.awaitShutdown(5000L);
		} catch (InterruptedException e) {
			log.error("Graceful shutdown container error:", e);
		}
	}
}
