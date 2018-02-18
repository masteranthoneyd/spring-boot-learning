package com.yangbingdong.springbootasyncandschedule.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author ybd
 * @date 18-2-18.
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@EnableAsync
public class SpringAsyncConfig implements AsyncConfigurer {

	@Bean
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(42);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("asyncExecutor-");
		executor.initialize();
		return executor;
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(8);
		executor.setMaxPoolSize(42);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("MyExecutor-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
		return (ex, method, params) -> {
			ExceptionUtils.printRootCauseStackTrace(ex);

			System.out.println("Exception message - " + ex.getMessage());
			System.out.println("Method name - " + method.getName());
			for (Object param : params) {
				System.out.println("Parameter value - " + param);
			}
		};
	}

}
