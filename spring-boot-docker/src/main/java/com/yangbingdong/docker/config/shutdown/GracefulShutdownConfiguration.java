package com.yangbingdong.docker.config.shutdown;

import io.undertow.server.HandlerWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author ybd
 * @date 18-4-19
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@ConditionalOnClass(HandlerWrapper.class)
public class GracefulShutdownConfiguration {

	@Bean
	public GracefulShutdownWrapper gracefulShutdownWrapper() {
		return new GracefulShutdownWrapper();
	}

	@Bean
	public UndertowServletWebServerFactory servletWebServerFactory() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo.addOuterHandlerChainWrapper(gracefulShutdownWrapper()));
//		factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_STATISTICS, true));
		return factory;
	}

	@Bean
	@Primary
	public GracefulShutdownListener gracefulShutdown() {
		return new GracefulShutdownListener(gracefulShutdownWrapper());
	}

}
