package com.yangbingdong.docker.pubsub.disruptor.core;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
public abstract class AbstractDisruptorEvent<SOURCE> {
	protected SOURCE s;

	public SOURCE getSource() {
		return this.s;
	}

	public void clean() {
		this.s = null;
	}
}
