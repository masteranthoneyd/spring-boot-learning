package com.yangbingdong.redis.redis;


import com.yangbingdong.redis.SpringDataRedisApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
public class RedisBaseOpsTest extends SpringDataRedisApplicationTests {

	@Autowired
	public StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	public void setStringTtl() {
		String key = "ybd";
		String value = "666";

		ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
		ops.set(key, value, Duration.ofMinutes(2));
		Assertions.assertThat(ops.get(key))
				  .isEqualTo(value);

		Long expire = stringRedisTemplate.getExpire(key);
		System.out.println("============  " + expire);
	}

	public static void main(String[] args) {
		Duration parse = Duration.parse("10000ms");
		System.out.println(parse.getSeconds());
	}
}
