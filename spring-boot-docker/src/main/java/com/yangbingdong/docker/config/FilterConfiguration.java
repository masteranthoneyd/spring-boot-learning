package com.yangbingdong.docker.config;

import com.yangbingdong.docker.filter.IpFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import static java.util.Collections.singletonList;

/**
 * @author ybd
 * @date 18-4-8
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class FilterConfiguration {
	@Bean
	public FilterRegistrationBean ipFilterRegistrationBean() {
		FilterRegistrationBean<IpFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new IpFilter());
		registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registrationBean.setUrlPatterns(singletonList("/*"));
		return registrationBean;
	}
}
