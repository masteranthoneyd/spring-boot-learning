package com.yangbingdong.springbootdisruptor.basic;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class LongEventHandler3 implements EventHandler<LongEvent> {
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
		System.out.println("c3: c1 has completed");
//		log.info("handle3 event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch);
	}
}
