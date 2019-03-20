package com.yangbingdong.redis.ops;

import com.yangbingdong.redis.util.ProtostuffSerializer;
import com.yangbingdong.redis.util.Serializer;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
public class BaseRedisOps<T> {

	private final Class<T> entityClass;
	private final RedisTemplate<String, T> redisTemplate;
	private final RedisSerializer<String> keySerializer;
	private final Serializer valueSerializer;


	public BaseRedisOps(RedisTemplate<String, T> redisTemplate, Class<T> entityClass) {
		keySerializer = RedisSerializer.string();
		valueSerializer = new ProtostuffSerializer();

		this.redisTemplate = redisTemplate;
		this.entityClass = entityClass;
	}

	public void set(String key, T value) {
		redisTemplate.execute((RedisCallback<T>) connection -> {
			connection.set(rawKey(key), rawValue(value));
			return null;
		});
	}


	public void set(String key, T value, long expire) {
		redisTemplate.execute((RedisCallback<T>) connection -> {
			connection.setEx(rawKey(key), expire, rawValue(value));
			return null;
		});
	}

	public T get(String key) {
		return redisTemplate.execute((RedisCallback<T>) connection -> {
			byte[] valueBytes = connection.get(rawKey(key));
			return valueSerializer.deserialize(valueBytes, entityClass);
		});
	}

	private byte[] rawKey(String key) {
		return keySerializer.serialize(key);
	}

	private byte[] rawValue(Object value) {
		return valueSerializer.serialize(value);
	}
}
