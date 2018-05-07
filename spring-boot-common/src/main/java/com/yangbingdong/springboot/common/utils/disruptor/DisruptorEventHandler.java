package com.yangbingdong.springboot.common.utils.disruptor;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
public interface DisruptorEventHandler<T extends DisruptorEvent> {
	int order();

	void onEvent(T event, long sequence, boolean endOfBatch, int currentShard) throws Exception;
}
