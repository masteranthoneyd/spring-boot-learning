package com.yangbingdong.springbootdisruptor.basic;


import com.lmax.disruptor.EventFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class LongEventFactory implements EventFactory<LongEvent> {
	private AtomicInteger count = new AtomicInteger(0);
	@Override
	public LongEvent newInstance() {
		log.info("logEventFactory create LongEvent... {}", count.incrementAndGet());
		return new LongEvent();
	}
}
