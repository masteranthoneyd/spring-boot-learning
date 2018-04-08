package com.yangbingdong.docker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ybd
 * @date 18-4-8
 * @contact yangbingdong1994@gmail.com
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class HelloController {

	@GetMapping("/hello")
	public String helloV1() {
		log.info("hello hello hello");
		return "hello";
	}

	@GetMapping("/hello2")
	public String helloV2() {
		log.info("hello2222222222222");
		return "hello2";
	}
}
