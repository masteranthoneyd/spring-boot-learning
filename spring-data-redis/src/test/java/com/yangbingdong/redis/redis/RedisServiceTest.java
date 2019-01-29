package com.yangbingdong.redis.redis;

import com.yangbingdong.redis.SpringDataRedisApplicationTests;
import com.yangbingdong.redis.service.UserService;
import com.yangbingdong.redis.vo.Sex;
import com.yangbingdong.redis.vo.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
public class RedisServiceTest extends SpringDataRedisApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void setAndGet() {
		User user = buildUser();
		String key = genKey(user);
		userService.set(key, user);
		User userInRedis = userService.get(key);
		Assertions.assertThat(user)
				  .isEqualTo(userInRedis);
	}

	private String genKey(User user) {
		return User.class.getSimpleName() + ":" + user.getName();
	}

	private static User buildUser() {
		return new User().setAge(20)
						 .setCreateTime(LocalDateTime.now())
						 .setUpdateTime(new Date())
						 .setEmail("6666666.com")
						 .setName("ybd")
						 .setSex(Sex.MAIL);
	}
}
