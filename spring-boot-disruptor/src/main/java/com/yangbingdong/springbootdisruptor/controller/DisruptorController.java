package com.yangbingdong.springbootdisruptor.controller;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.yangbingdong.springbootdisruptor.basic.LongEvent;
import com.yangbingdong.springbootdisruptor.basic.LongEventProducerWithTranslator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

/**
 * @author ybd
 * @date 18-2-5
 * @contact yangbingdong1994@gmail.com
 */
@RestController
public class DisruptorController {
	private Disruptor<LongEvent> disruptor;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void construct() {
		disruptor = new Disruptor<>(LongEvent::new, 1 << 5, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());

		disruptor.handleEventsWith((EventHandler<LongEvent>) (event, sequence, endOfBatch) -> {
			if (event.getValue() == 3) {
				throw new IllegalArgumentException("这是一个模拟异常");
			} else {
				System.out.println("event: " + event);
			}
		});

		disruptor.setDefaultExceptionHandler(new ExceptionHandler<LongEvent>() {
			@Override
			public void handleEventException(Throwable ex, long sequence, LongEvent event) {
				System.out.println("捕捉异常：" + ex.getMessage());
				System.out.println("处理异常逻辑...");
			}

			@Override
			public void handleOnStartException(Throwable ex) {
				System.out.println("handleOnStartException");
			}

			@Override
			public void handleOnShutdownException(Throwable ex) {
				System.out.println("handleOnShutdownException");
			}
		});

		disruptor.start();
	}

	@GetMapping("/produce/{l}")
	public String produce(@PathVariable Long l) {
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		LongEventProducerWithTranslator longEventProducerWithTranslator = new LongEventProducerWithTranslator();

		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putLong(0, l);
		ringBuffer.publishEvent(longEventProducerWithTranslator, bb);

		return "produce SUCCESS";
	}

	@GetMapping("/stop")
	public String stop() {
		disruptor.shutdown();
		return "stop SUCCESS";
	}

	@GetMapping("/info")
	public String start() {
		return disruptor.toString();
	}


}
