package com.yangbingdong.springbootdisruptor.basic;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.LongStream;

import static com.yangbingdong.springbootcommon.utils.function.Trier.tryLongConsumer;

/**
 * @author ybd
 * @date 18-1-31
 * @contact yangbingdong@1994.gmail
 */
@Slf4j
public class LongEventDisruptorBasicTest {

	@Test
	public void singleProducerLongEventDefaultTest() throws InterruptedException {
		// Executor that will be used to construct new threads for consumers
		Executor executor = Executors.newCachedThreadPool();

		// The factory for the event
		LongEventFactory factory = new LongEventFactory();

		// Specify the size of the ring buffer, must be power of 2.
		int bufferSize = 1 << 3;

		// Construct the Disruptor
		Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, executor, ProducerType.SINGLE, new BlockingWaitStrategy());

		// Connect the handler
		disruptor.handleEventsWith(new LongEventHandler());

		// Start the Disruptor, starts all threads running
		disruptor.start();

		// Get the ring buffer from the Disruptor to be used for publishing.
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		LongEventProducer producer = new LongEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; l < 100; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
			Thread.sleep(10);
		}
	}

	@Test
	public void singleProducerLongEventUseThreadFactoryTest() throws InterruptedException {
		ThreadFactory threadFactory = new ThreadFactory() {
			private final AtomicInteger index = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(null, r, "disruptor-thread-" + index.getAndIncrement());
			}
		};

		LongEventFactory factory = new LongEventFactory();

		int bufferSize = 1 << 3;

		Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());

		disruptor.handleEventsWith(new LongEventHandler());

		disruptor.start();

		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		LongEventProducer producer = new LongEventProducer(ringBuffer);

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; l < 100; l++) {
			bb.putLong(0, l);
			producer.onData(bb);
			Thread.sleep(10);
		}
	}

	@Test
	public void singleProducerLongEventUseTranslatorsTest() throws InterruptedException {
		ThreadFactory threadFactory = new ThreadFactory() {
			private final AtomicInteger index = new AtomicInteger(1);

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(null, r, "disruptor-thread-" + index.getAndIncrement());
			}
		};

		LongEventFactory factory = new LongEventFactory();

		int bufferSize = 1 << 3;

		Disruptor<LongEvent> disruptor = new Disruptor<>(factory, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());

		disruptor.handleEventsWith(new LongEventHandler());

		disruptor.start();

		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		ByteBuffer bb = ByteBuffer.allocate(8);
		for (long l = 0; l < 100; l++) {
			bb.putLong(0, l);
			ringBuffer.publishEvent(longEventProducerWithTranslator, bb);
			Thread.sleep(10);
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void singleProducerLongEventJava8Test() {
		int bufferSize = 1 << 3;

		Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, (ThreadFactory) Thread::new, ProducerType.SINGLE, new BlockingWaitStrategy());

		disruptor.handleEventsWith((event, sequence, endOfBatch) -> log.info("handle event: {}, sequence: {}, endOfBatch: {}", event, sequence, endOfBatch));

		disruptor.start();

		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

		ByteBuffer bb = ByteBuffer.allocate(8);
		LongStream.range(0, 100)
				  .forEach(tryLongConsumer(l -> {
					  bb.putLong(0, l);
					  ringBuffer.publishEvent((event, sequence, buffer) -> event.setValue(buffer.getLong(0)), bb);
					  Thread.sleep(10);
				  }));
	}

}