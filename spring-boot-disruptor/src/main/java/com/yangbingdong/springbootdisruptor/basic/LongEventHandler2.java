package com.yangbingdong.springbootdisruptor.basic;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong@1994.gmail
 */
@Slf4j
public class LongEventHandler2 implements EventHandler<LongEvent> {
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
		log.info("handle2 event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch);
	}
}
