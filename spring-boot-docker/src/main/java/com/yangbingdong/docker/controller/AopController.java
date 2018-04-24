package com.yangbingdong.docker.controller;

import com.yangbingdong.docker.aop.ReqLog;
import com.yangbingdong.docker.controller.webvo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ybd
 * @date 18-4-20
 * @contact yangbingdong1994@gmail.com
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/aop")
public class AopController {

	/**
	 * http://192.168.6.113:8080/aop/1/bbb?name=ybd&age=24
	 */
	@ReqLog("这是aop1")
	@GetMapping("/1/{arg}")
	public UserVo aop1(@PathVariable("arg") String arg, @ModelAttribute("userVo") UserVo userVo) {
		log.info("aop controller 1 receive: {}", arg);
		return userVo;
	}

	@ReqLog("这是aop2")
	@GetMapping("/2/{arg}")
	public String aop2(@PathVariable("arg") String arg) {
		log.info("aop controller 2 receive: {}", arg);
//		throw new IllegalArgumentException("this is a mock exception!");
		return "arg";
	}
}
