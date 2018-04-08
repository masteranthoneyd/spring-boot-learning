package com.yangbingdong.docker.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ybd
 * @date 18-4-8
 * @contact yangbingdong1994@gmail.com
 */

@Plugin(name = "spring", category = StrLookup.CATEGORY)
public class MyEnvironmentAware extends AbstractLookup{
	private Map<String, String> environment;

	public MyEnvironmentAware() {
		if (environment == null) {
			environment = new HashMap<>();
		}
		environment.put("spring.application.name", "async-server");
	}

	@Override
	public String lookup(LogEvent event, String key) {
		if (environment != null) {
			return environment.get(key);
		}
		return null;
	}
}
