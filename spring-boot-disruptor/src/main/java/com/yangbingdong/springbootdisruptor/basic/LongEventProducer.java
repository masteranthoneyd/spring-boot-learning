package com.yangbingdong.springbootdisruptor.basic;


import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong1994@gmail.com
 */
public class LongEventProducer {
	private final RingBuffer<LongEvent> ringBuffer;

	public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}

	public void onData(ByteBuffer bb) {
		// Grab the next sequence
		long sequence = ringBuffer.next();
		try {
			// Get the entry in the Disruptor
			LongEvent event = ringBuffer.get(sequence);
			// for the sequence
			// Fill with data
			event.setValue(bb.getLong(0));
		} finally {
			ringBuffer.publish(sequence);
		}
	}
}
