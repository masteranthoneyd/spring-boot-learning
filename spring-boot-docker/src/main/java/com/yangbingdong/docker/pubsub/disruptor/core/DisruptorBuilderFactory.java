package com.yangbingdong.docker.pubsub.disruptor.core;

import com.yangbingdong.docker.pubsub.disruptor.event.AopLogEvent;
import com.yangbingdong.docker.pubsub.disruptor.event.DisruptorEvent;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
public class DisruptorBuilderFactory {
	private DisruptorBuilderFactory() {
	}

	public static DisruptorBuilderFactory getInstance() {
		return SingletonHolder.factory;
	}

	private static class SingletonHolder{
		private static DisruptorBuilderFactory factory = new DisruptorBuilderFactory();
	}

	public <T extends DisruptorEvent> DisruptorBuilder<T> getBuilder(Class<T> t) {
		return new DisruptorBuilder<>();
	}

	public static void main(String[] args) {
//		DisruptorBuilder<DisruptorEvent> builder = DisruptorBuilderFactory.getInstance().getBuilder();
		DisruptorBuilder<AopLogEvent> builder = DisruptorBuilderFactory.getInstance().getBuilder(AopLogEvent.class);
	}
}
