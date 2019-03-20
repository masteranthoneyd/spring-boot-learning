package com.yangbingdong.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class RedisConfig {

	@Bean("stringRedisSerializer")
	public RedisSerializer<String> stringRedisSerializer() {
		return RedisSerializer.string();
	}

	@Bean("redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<String> stringRedisSerializer) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		return template;
	}

}
