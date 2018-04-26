package com.yangbingdong.docker.pubsub.disruptor;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
public interface DisruptorLauncher<A> {
	void launch(A a);
}
