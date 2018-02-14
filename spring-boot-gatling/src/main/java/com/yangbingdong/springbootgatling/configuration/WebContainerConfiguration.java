package com.yangbingdong.springbootgatling.configuration;

import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ybd
 * @date 18-2-8
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class WebContainerConfiguration {

	/**
	 * 支持HTTP2
	 * @return
	 */
	@Bean
	public UndertowServletWebServerFactory embeddedServletContainerFactory() {
		UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
		/* 开启http2 */
		/* 1.4以上版本的Undertow默认已经支持http2了！ */
//		factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));

		/* 监听多个端口 */
		factory.addBuilderCustomizers(builder -> builder.addHttpListener(7070, "0.0.0.0"));
		return factory;
	}
}
