package com.yangbingdong.docker.pubsub.disruptor.handler;

/**
 * @author ybd
 * @date 18-4-27
 * @contact yangbingdong1994@gmail.com
 */
public abstract class AbstractShardingHandler<T> implements DisruptorHandler<T> {
	protected int currentShard = 0;
	protected int shardingQuantity;
	@Override
	public void onEvent(T event, long sequence, boolean endOfBatch) throws Exception {
		if (sequence % shardingQuantity == currentShard) {
			onShardingEvent(event, sequence, endOfBatch, currentShard);
		}
	}

	public abstract void onShardingEvent (T event, long sequence, boolean endOfBatch, int currentShard) throws Exception;

	public abstract AbstractShardingHandler addShardingQuantity();

	public int getCurrentShard() {
		return currentShard;
	}

	public AbstractShardingHandler setCurrentShard(int currentShard) {
		this.currentShard = currentShard;
		return this;
	}

	public int getShardingQuantity() {
		return shardingQuantity;
	}

	public AbstractShardingHandler setShardingQuantity(int shardingQuantity) {
		this.shardingQuantity = shardingQuantity;
		return this;
	}
}
