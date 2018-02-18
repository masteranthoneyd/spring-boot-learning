package com.yangbingdong.springbootasyncandschedule.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @author ybd
 * @date 18-2-18.
 * @contact yangbingdong1994@gmail.com
 */
@Service("someService")
public class SomeService implements ISomeService{

	@Async
	@Override
	public Future<String> asyncMethodWithVoidReturnType() throws InterruptedException {
//		int i = 1/0;
		Thread.sleep(2000L);
//		System.out.println("Execute method asynchronously. " + Thread.currentThread().getName());
		return AsyncResult.forValue("Execute method asynchronously. " + Thread.currentThread().getName());
	}

	private int i = 0;

	@Override
	@Scheduled(fixedDelay=1000)
	public void doScheduled() {
		System.out.println(Thread.currentThread().getName() + "  " + ++i);
	}
}
