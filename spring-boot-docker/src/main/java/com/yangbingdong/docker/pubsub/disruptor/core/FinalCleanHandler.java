package com.yangbingdong.docker.pubsub.disruptor.core;

import com.lmax.disruptor.EventHandler;

/**
 * @author ybd
 * @date 18-5-7
 * @contact yangbingdong1994@gmail.com
 */
public class FinalCleanHandler<S> implements EventHandler<DisruptorEvent<S>> {
	@Override
	public void onEvent(DisruptorEvent<S> event, long sequence, boolean endOfBatch) throws Exception {
		event.clean();
	}
}
