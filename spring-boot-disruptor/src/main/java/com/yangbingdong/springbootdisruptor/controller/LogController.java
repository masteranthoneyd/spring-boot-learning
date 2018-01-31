package com.yangbingdong.springbootdisruptor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ybd
 * @date 18-1-30
 * @contact yangbingdong@1994.gmail
 */
@RestController
@Slf4j
public class LogController {
	@GetMapping("/log")
	public String getLog() {
		log.trace("this is trace");
		log.debug("this is debug");
		log.info("this is info");
		log.warn("this is warn");
		log.error("this is error");
		return "log";
	}
}
