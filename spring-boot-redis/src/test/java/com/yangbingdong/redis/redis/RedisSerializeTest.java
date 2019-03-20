package com.yangbingdong.redis.redis;


import com.yangbingdong.redis.SpringDataRedisApplicationTests;
import com.yangbingdong.redis.util.ProtostuffSerializer;
import com.yangbingdong.redis.util.Serializer;
import com.yangbingdong.redis.vo.Sex;
import com.yangbingdong.redis.vo.User;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static java.util.Collections.singletonList;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
public class RedisSerializeTest extends SpringDataRedisApplicationTests {

	private Serializer serializer = new ProtostuffSerializer();

	@Test
	public void serializeTest() {
		val user = buildUser();
		byte[] serialize = serializer.serialize(user);
		Assertions.assertThat(serialize)
				  .isNotEmpty();
		User deUser = serializer.deserialize(serialize, User.class);
		Assertions.assertThat(user)
				  .isEqualTo(deUser);

	}

	private static User buildUser() {
		return new User().setAge(20)
						 .setCreateTime(LocalDateTime.now())
						 .setUpdateTime(new Date())
						 .setEmail("6666666.com")
						 .setName("ybd")
						 .setSex(Sex.MAIL)
						 .setFriends(singletonList(new User().setAge(19)
															 .setEmail("77777777.com")
															 .setName("yqy")
															 .setSex(Sex.MAIL)));
	}
}
