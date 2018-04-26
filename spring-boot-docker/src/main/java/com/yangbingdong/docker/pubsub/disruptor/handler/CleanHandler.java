package com.yangbingdong.docker.pubsub.disruptor.handler;

import com.lmax.disruptor.EventHandler;
import com.yangbingdong.docker.pubsub.disruptor.event.DisruptorEvent;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class CleanHandler implements EventHandler<DisruptorEvent> {
	@Override
	public void onEvent(DisruptorEvent event, long sequence, boolean endOfBatch) {
		event.clean();
	}
}
