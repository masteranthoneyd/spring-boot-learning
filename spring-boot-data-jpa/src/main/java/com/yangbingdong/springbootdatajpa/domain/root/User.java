package com.yangbingdong.springbootdatajpa.domain.root;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

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
	public static final String EMAIL = "email";

	@NotBlank(message = "姓名不能为空")
	private String name;
	private String email;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			name = "userRole",
			joinColumns = @JoinColumn(name = "userId", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="roleId",referencedColumnName="id")
	)
	private List<Role> roles;

	@Override
	public User setCreateTime(LocalDateTime createTime) {
		super.setCreateTime(createTime);
		return this;
	}
}
