package com.yangbingdong.docker.config;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.lookup.AbstractLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.yangbingdong.springbootcommon.utils.StringUtil.isNotBlank;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * @author ybd
 * @date 18-4-9
 * @contact yangbingdong1994@gmail.com
 */
@Plugin(name = "spring", category = StrLookup.CATEGORY)
public class SpringEnvironmentLookup extends AbstractLookup {
	private LinkedHashMap ymlData;
	private Map<String, String> map;
	private static final String APPLICATION_YML_NAME = "application.yml";

	public SpringEnvironmentLookup() {
		super();
		map = new HashMap<>(16);
		try {
			ymlData = new Yaml().loadAs(new ClassPathResource(APPLICATION_YML_NAME).getInputStream(), LinkedHashMap.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("SpringEnvironmentLookup initialize fail");
		}
	}

	@Override
	public String lookup(LogEvent event, String key) {
		return map.computeIfAbsent(key, this::resolveYmlMapByKey);
	}

	private String resolveYmlMapByKey(String key) {
		Assert.isTrue(isNotBlank(key), "key can not be blank!");
		String[] keyChain = key.split("\\.");
		int length = keyChain.length;
		if (length == 1) {
			return map.computeIfAbsent(key, s -> getFinalValue(s, ymlData));
		}
		String k;
		LinkedHashMap[] mapChain = new LinkedHashMap[length];
		mapChain[0] = ymlData;
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				int finalI = i;
				return map.computeIfAbsent(key, s -> getFinalValue(keyChain[finalI], mapChain[finalI]));
			}
			k = keyChain[i];
			Object o = mapChain[i].get(k);
			if (o instanceof LinkedHashMap) {
				mapChain[i + 1] = (LinkedHashMap) o;
			}else {
				throw new IllegalArgumentException();
			}
		}
		return "";
	}

	private static String getFinalValue(String k, LinkedHashMap ymlData) {
		return defaultIfNull((String) ymlData.get(k), "");
	}
}
