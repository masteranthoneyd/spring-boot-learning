package com.yangbingdong.springboothotdeploy.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ybd
 * @date 18-1-16
 * @contact yangbingdong1994@gmail.com
 */
@RestController
@RequestMapping("/hello")
@Log4j2
public class HelloController {

	@GetMapping
	public String getHello() {
		log.info("666666666666666666666666666666666666");
		return "Hello V66";
	}
}
