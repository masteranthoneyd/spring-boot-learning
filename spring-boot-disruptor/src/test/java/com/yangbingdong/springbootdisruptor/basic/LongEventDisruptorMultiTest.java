package com.yangbingdong.springbootdisruptor.basic;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong@1994.gmail
 */
@Slf4j
public class LongEventDisruptorMultiTest {

	@Test
	public void multiCustomerOneProducerTest() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());

		LongEventHandler c1 = new LongEventHandler();
		LongEventHandler2 c2 = new LongEventHandler2();
		LongEventHandler3 c3 = new LongEventHandler3();

		disruptor.handleEventsWith(c1, c2).then(c3);

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 100)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void exceptionTest() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new YieldingWaitStrategy());

		disruptor.handleEventsWith((EventHandler<LongEvent>) (event, sequence, endOfBatch) -> {
			if (sequence == 3) {
				throw new IllegalArgumentException("非法！！！！！！！！");
			}else {
				System.out.println("event: " + event);
			}
		});

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 100)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void multiProducerOneCustomerTest() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(30);

		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new YieldingWaitStrategy());

		disruptor.handleEventsWith((event, sequence, endOfBatch) -> {
			/* 由于log4j2本身是异步的，所以有可能第一个handle读取到的是handle2已经处理过的event */
			System.out.println("handle event: " + event);
//			log.info("handle event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch);
		}).then((event, sequence, endOfBatch) -> {
			event.setValue(event.getValue() + 1000);
			System.out.println("handle event: " + event);
//			log.info("handle2 event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch);
			countDownLatch.countDown();
		});

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 10)).start();
		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 10, 20)).start();
		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 20, 30)).start();

		countDownLatch.await();
	}

	private void produce(Disruptor<LongEvent> disruptor, LongEventProducerWithTranslator longEventProducerWithTranslator, int i, int i2) {
		try {
			RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

			ByteBuffer bb = ByteBuffer.allocate(8);
			for (long l = i; l < i2; l++) {
				bb.putLong(0, l);
				ringBuffer.publishEvent(longEventProducerWithTranslator, bb);
				TimeUnit.MILLISECONDS.sleep(20);
			}
		} catch (Exception e) {
			System.out.println("catch error.............");
			e.printStackTrace();
		}
	}
}
