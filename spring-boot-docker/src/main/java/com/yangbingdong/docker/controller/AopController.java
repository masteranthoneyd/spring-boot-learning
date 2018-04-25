package com.yangbingdong.docker.controller;

import com.yangbingdong.docker.aop.ReqLog;
import com.yangbingdong.docker.controller.webvo.UserVo;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.domain.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

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

	private final AccessLogRepository accessLogRepository;

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
		throw new IllegalArgumentException("{\"respBody\":{\"showTime\":5,\"advert\":[{\"pic\":\"\",\"type\":1},{\"pic\":\"\",\"type\":0}]},\"respHeader\":{\"resultCode\":0,\"message\":\"正确执行\"}}");
//		return "arg";
	}

	@ReqLog("这是aop3")
	@GetMapping("/3/{arg}")
	public UserVo aop3(@PathVariable("arg") String arg, @ModelAttribute("userVo") UserVo userVo) {
		int i = 1 / 0;
		log.info("aop controller 1 receive: {}", arg);
		return userVo;
	}

	@ReqLog("这是aop4")
	@GetMapping("/4/{arg}")
	public UserVo aop4(@PathVariable("arg") String arg) {
		throw new AsyncRequestTimeoutException();
	}

	@GetMapping("/{id}")
	public AccessLog getById(@PathVariable("id") Long id) {
		return accessLogRepository.findById(id).orElse(null);
	}
}
