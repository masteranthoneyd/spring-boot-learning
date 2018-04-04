package com.yangbingdong.docker.config;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ybd
 * @date 18-4-4
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class MDCInjector {
	@Value("${spring.application.name}")
	private String applicationName;

	@PostConstruct
	public void init() {
		Map<String, String> map = new HashMap<>();
		map.put("applicationName", applicationName);
		MDC.setContextMap(map);
	}
}
