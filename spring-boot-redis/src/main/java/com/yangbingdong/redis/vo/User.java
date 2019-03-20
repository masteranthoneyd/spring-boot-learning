package com.yangbingdong.redis.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author ybd
 * @date 19-1-29
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class User {
	private String name;
	private String email;
	private Integer age;
	private Sex sex;
	private LocalDateTime createTime;
	private Date updateTime;
	private List<User> friends;
}
