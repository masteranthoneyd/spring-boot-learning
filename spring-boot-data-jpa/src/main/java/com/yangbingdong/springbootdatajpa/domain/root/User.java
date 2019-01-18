package com.yangbingdong.springbootdatajpa.domain.root;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Entity
public class User extends BaseEntity {
	public static final String NAME = "name";

	@NotBlank(message = "姓名不能为空")
	private String name;
	private String email;

	@Override
	public User setCreateTime(LocalDateTime createTime) {
		super.setCreateTime(createTime);
		return this;
	}
}
