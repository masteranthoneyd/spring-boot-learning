package com.yangbingdong.springbootdatajpa.domain.root;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author ybd
 * @date 18-3-28
 * @contact yangbingdong1994@gmail.com
 */
@Data
public class Foo {
	@NotBlank(message = "姓名不能为空")
	private String name;

	@Min(18)
	private Integer age;

	@Pattern(regexp = "^1([34578])\\d{9}$",message = "手机号码格式错误")
	@NotBlank(message = "手机号码不能为空")
	private String phone;

	@Email(message = "邮箱格式错误")
	private String email;
}
