package com.yangbingdong.springbootgatling.configuration;

import org.springframework.context.annotation.Configuration;

/**
 * @author ybd
 * @date 18-2-8
 * @contact yangbingdong@1994.gmail
 */
@Configuration
public class WebContainerConfiguration {

	/**
	 * 支持HTTP2
	 * @return
	 */
	/*@Bean
	UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
		UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
		factory.addBuilderCustomizers(builder -> builder.setServerOption(UndertowOptions.ENABLE_HTTP2, true));
		return factory;
	}*/
}
