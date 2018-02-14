package com.yangbingdong.springbootdisruptor.basic;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
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
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class LongEventDisruptorMultiTest {

	@SuppressWarnings("unchecked")
	@Test
	public void multiCustomerOneProducerTest() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new LiteBlockingWaitStrategy());

		LongEventHandler c1 = new LongEventHandler();
		LongEventHandler2 c2 = new LongEventHandler2();

		disruptor.handleEventsWith(c1, c2)
				 .then((EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("c1 and c2 has completed \n"));

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 100)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void multiCustomerOneProducerTest2() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new LiteBlockingWaitStrategy());

		LongEventHandler c1a = new LongEventHandler();
		LongEventHandler2 c2a = new LongEventHandler2();
		LongEventHandler3 c1b = new LongEventHandler3();
		LongEventHandler4 c2b = new LongEventHandler4();

		disruptor.handleEventsWith(c1a, c2a);
		disruptor.after(c1a).then(c1b);
		disruptor.after(c2a).then(c2b);
		disruptor.after(c1b, c2b)
				 .then((EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("last costumer \n"));

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 30)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void multiCustomerOneProducerTest3() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE, new LiteBlockingWaitStrategy());

		EventHandler a = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process a... event: " + event);
		EventHandler b = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process b... event: " + event);
		EventHandler c = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process c... event: " + event);
		EventHandler d = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process d... event: " + event);
		EventHandler e = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process e... a,b,c has completed, event: " + event + "\n");
		EventHandler f = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process f... d has completed, event: " + event + "\n");
		EventHandler g = (EventHandler<LongEvent>) (event, sequence, endOfBatch) -> System.out.println("process g... e,f has completed, event: " + event + "\n\n");

		disruptor.handleEventsWith(a, b, c, d);
		disruptor.after(a, b, c).then(e);
		disruptor.after(d).then(f);
		disruptor.after(e, f).then(g);

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		disruptor.start();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 2)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void exceptionTest() throws InterruptedException {
		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, DaemonThreadFactory.INSTANCE, ProducerType.SINGLE, new LiteBlockingWaitStrategy());

		disruptor.handleEventsWith((EventHandler<LongEvent>) (event, sequence, endOfBatch) -> {
			if (sequence == 3) {
				throw new IllegalArgumentException("这是一个模拟异常");
			} else {
				System.out.println("event: " + event);
			}
		});

		disruptor.start();

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		new Thread(() -> produce(disruptor, longEventProducerWithTranslator, 0, 100)).start();

		TimeUnit.SECONDS.sleep(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void multiProducerOneCustomerTest() throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(30);

		int bufferSize = 1 << 8;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.defaultThreadFactory(), ProducerType.MULTI, new LiteBlockingWaitStrategy());

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
