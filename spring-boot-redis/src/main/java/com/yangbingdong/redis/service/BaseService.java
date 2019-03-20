package com.yangbingdong.redis.service;

import com.yangbingdong.redis.ops.BaseRedisOps;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.ParameterizedType;

import static com.alibaba.fastjson.util.TypeUtils.getGenericParamType;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
public class BaseService<T> implements InitializingBean {

	@Getter
	private BaseRedisOps<T> redisOps;

	@Autowired
	private RedisTemplate redisTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void afterPropertiesSet() {
		ParameterizedType genericParamType = (ParameterizedType) getGenericParamType(this.getClass());
		redisOps = new BaseRedisOps<>(redisTemplate, (Class<T>) genericParamType.getActualTypeArguments()[0]);
	}

	public void set(String key, T t) {
		redisOps.set(key, t);
	}

	public void set(String key, T t, long expire) {
		redisOps.set(key, t, expire);
	}

	public T get(String key) {
		return redisOps.get(key);
	}
}
