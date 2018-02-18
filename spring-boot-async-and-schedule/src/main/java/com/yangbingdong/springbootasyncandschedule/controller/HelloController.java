package com.yangbingdong.springbootasyncandschedule.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author ybd
 * @date 18-2-18.
 * @contact yangbingdong1994@gmail.com
 */
@AllArgsConstructor
@RestController
public class HelloController {

	private final ISomeService someService;

//	private Executor threadPoolTaskExecutor;

	@GetMapping("/hello")
	public Mono<String> syaHello() throws InterruptedException, ExecutionException {
		Future<String> stringFuture = someService.asyncMethodWithVoidReturnType();
		while (!stringFuture.isDone()){
			System.out.println("wait...");
			Thread.sleep(500L);
		}
		System.out.println(stringFuture.get());
		return Mono.just("Hello World");
	}

	/*@PostConstruct
	public void init() {
		System.out.println("6666666666: " + threadPoolTaskExecutor);
	}*/
}
