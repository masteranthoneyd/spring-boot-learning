package com.yangbingdong.springbootasyncandschedule.controller;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.Future;

/**
 * @author ybd
 * @date 18-2-18.
 * @contact yangbingdong1994@gmail.com
 */
public interface ISomeService {
	Future<String> asyncMethodWithVoidReturnType() throws InterruptedException;

	@Scheduled(fixedDelay=1000)
	void doScheduled();
}
