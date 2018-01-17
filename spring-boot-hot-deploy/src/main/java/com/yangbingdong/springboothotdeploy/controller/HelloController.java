package com.yangbingdong.springboothotdeploy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ybd
 * @date 18-1-16
 * @contact yangbingdong@1994.gmail
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

	@GetMapping
	public String getHello() {
		return "Hello V66";
	}
}
