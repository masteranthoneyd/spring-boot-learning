package com.yangbingdong.docker.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.currentThread;
import static java.util.UUID.randomUUID;

/**
 * @author ybd
 * @date 18-4-3
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
@Component
public class LogIntervalSender {
	private AtomicInteger atomicInteger = new AtomicInteger(0);

//	@Scheduled(fixedDelay = 2000)
	public void doScheduled() {
		try {
			int i = atomicInteger.incrementAndGet();
			randomThrowException(i);
			log.info("{} send a message: the sequence is {} , random uuid is {}", currentThread().getName(), i, randomUUID());
		} catch (Exception e) {
			log.error("catch an exception:", e);
		}
	}

	private void randomThrowException(int i) {
		if (i % 10 == 0) {
			throw new RuntimeException("this is a random exception, sequence = " + i);
		}
	}
}
