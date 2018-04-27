package com.yangbingdong.docker.pubsub.disruptor.handler;

import com.lmax.disruptor.EventHandler;

/**
 * @author ybd
 * @date 18-4-26
 * @contact yangbingdong1994@gmail.com
 */
public interface DisruptorHandler<T> extends EventHandler<T> {
	int horizontalOrder();
}
