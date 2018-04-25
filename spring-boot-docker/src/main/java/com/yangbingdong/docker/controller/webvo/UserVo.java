package com.yangbingdong.docker.controller.webvo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 18-4-24
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class UserVo {
	private Integer age;
	private String name;
}
