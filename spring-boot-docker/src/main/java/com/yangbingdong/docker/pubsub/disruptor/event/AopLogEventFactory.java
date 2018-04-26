package com.yangbingdong.docker.pubsub.disruptor.event;

import com.lmax.disruptor.EventFactory;
import org.springframework.stereotype.Component;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
@Component
public class AopLogEventFactory implements EventFactory<AopLogEvent> {
	@Override
	public AopLogEvent newInstance() {
		return new AopLogEvent();
	}
}
