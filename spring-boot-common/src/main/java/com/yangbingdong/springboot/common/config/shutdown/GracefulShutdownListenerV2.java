package com.yangbingdong.springboot.common.config.shutdown;

import io.undertow.Undertow;
import io.undertow.server.ConnectorStatistics;
import io.undertow.server.handlers.GracefulShutdownHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author ybd
 * @date 18-4-19
 * @contact yangbingdong1994@gmail.com
 */
@RequiredArgsConstructor
@Slf4j
public class GracefulShutdownListenerV2 implements ApplicationListener<ContextClosedEvent> {

	private final GracefulShutdownWrapper gracefulShutdownWrapper;

	private final ServletWebServerApplicationContext context;

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		GracefulShutdownHandler gracefulShutdownHandler = gracefulShutdownWrapper.getGracefulShutdownHandler();
		try {
			gracefulShutdownHandler.shutdown();
			UndertowServletWebServer webServer = (UndertowServletWebServer)context.getWebServer();
			Field field = webServer.getClass().getDeclaredField("undertow");
			field.setAccessible(true);
			Undertow undertow = (Undertow) field.get(webServer);
			List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
			Undertow.ListenerInfo listener = listenerInfo.get(0);
			ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
			while (connectorStatistics.getActiveConnections() > 0){}
		} catch (Exception e) {
			log.error("Graceful shutdown container error:", e);
		}
	}
}
