package com.yangbingdong.docker.pubsub.disruptor.handler;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
public interface DisruptorHandler {
	default Integer order(){
		return Integer.MIN_VALUE;
	}
}
