package com.yangbingdong.docker.pubsub.disruptor.core;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
public class DisruptorEvent<SOURCE> {
	protected SOURCE s;

	public SOURCE getSource() {
		return this.s;
	}

	public void setSource(SOURCE s) {
		this.s = s;
	}

	public void clean() {
		this.s = null;
	}
}
