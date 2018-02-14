package com.yangbingdong.springbootdisruptor.basic;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class LongEventHandler5 implements EventHandler<LongEvent> {
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
		log.info("handle3 event: {}, sequence: {}, endOfBatch: {} \n", event, sequence, endOfBatch);
	}
}
