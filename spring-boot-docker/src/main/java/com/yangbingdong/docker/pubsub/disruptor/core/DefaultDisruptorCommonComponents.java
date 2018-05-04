package com.yangbingdong.docker.pubsub.disruptor.core;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class DefaultDisruptorCommonComponents {
	private int bufferSizePower = 18;
	private ThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("disruptor-%d")
																		  .daemon(true)
																		  .build();
	private ProducerType producerType = ProducerType.SINGLE;
	private WaitStrategy waitStrategy = new BlockingWaitStrategy();
}
