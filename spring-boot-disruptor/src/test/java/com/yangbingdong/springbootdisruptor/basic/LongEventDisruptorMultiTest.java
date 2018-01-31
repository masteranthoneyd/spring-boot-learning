package com.yangbingdong.springbootdisruptor.basic;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadFactory;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong@1994.gmail
 */
@Slf4j
public class LongEventDisruptorMultiTest {

	@SuppressWarnings("unchecked")
	@Test
	public void multiProducerOneCustomerTest() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(30);

		int bufferSize = 1 << 6;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, (ThreadFactory) Thread::new, ProducerType.SINGLE, new BlockingWaitStrategy());

		disruptor.handleEventsWith((event, sequence, endOfBatch) -> log.info("handle event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch));

		disruptor.start();

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		new Thread(() -> {
			RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

			ByteBuffer bb = ByteBuffer.allocate(8);
			for (long l = 0; l < 10; l++) {
				bb.putLong(0, l);
				ringBuffer.publishEvent(longEventProducerWithTranslator, bb);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				countDownLatch.countDown();
			}
		}).start();
		new Thread(() -> {
			RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

			ByteBuffer bb = ByteBuffer.allocate(8);
			for (long l = 10; l < 20; l++) {
				bb.putLong(0, l);
				ringBuffer.publishEvent(longEventProducerWithTranslator, bb);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				countDownLatch.countDown();
			}
		}).start();
		new Thread(() -> {
			RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

			ByteBuffer bb = ByteBuffer.allocate(8);
			for (long l = 20; l < 30; l++) {
				bb.putLong(0, l);
				ringBuffer.publishEvent(longEventProducerWithTranslator, bb);

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				countDownLatch.countDown();
			}
		}).start();

		countDownLatch.await();
	}
}
