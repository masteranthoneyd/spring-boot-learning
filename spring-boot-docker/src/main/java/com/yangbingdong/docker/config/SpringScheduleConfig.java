package com.yangbingdong.docker.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author ybd
 * @date 18-2-18.
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
@EnableScheduling
public class SpringScheduleConfig implements SchedulingConfigurer {

	@SuppressWarnings("NullableProblems")
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	@Bean
	public Executor taskExecutor() {
		return new ScheduledThreadPoolExecutor(4,
				new BasicThreadFactory
						.Builder()
						.namingPattern("schedule-thread-%d")
						.daemon(true)
						.build());
	}

}
