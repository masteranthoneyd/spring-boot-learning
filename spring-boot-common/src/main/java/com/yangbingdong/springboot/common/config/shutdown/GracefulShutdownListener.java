package com.yangbingdong.springboot.common.config.shutdown;

import io.undertow.server.handlers.GracefulShutdownHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import static com.yangbingdong.springboot.common.utils.function.Trier.tryConsumer;
import static java.util.Optional.ofNullable;

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
		try {
			ofNullable(gracefulShutdownWrapper.getGracefulShutdownHandler())
					.ifPresent(tryConsumer(this::shutdownInternal));
		} catch (Exception e) {
			log.error("Graceful shutdown container error:", e);
		}
	}

	private void shutdownInternal(GracefulShutdownHandler handler) throws InterruptedException {
		handler.shutdown();
		handler.awaitShutdown(5000L);
	}
}
