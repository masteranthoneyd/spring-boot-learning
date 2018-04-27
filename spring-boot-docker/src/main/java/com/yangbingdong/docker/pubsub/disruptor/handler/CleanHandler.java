package com.yangbingdong.docker.pubsub.disruptor.handler;

import com.yangbingdong.docker.pubsub.disruptor.event.DisruptorEvent;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
public class CleanHandler implements DisruptorHandler<DisruptorEvent> {
	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) {
		event.clean();
	}

	@Override
	public int horizontalOrder() {
		return Integer.MAX_VALUE;
	}
}
