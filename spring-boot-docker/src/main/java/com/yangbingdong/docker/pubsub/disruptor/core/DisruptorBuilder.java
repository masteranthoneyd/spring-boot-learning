package com.yangbingdong.docker.pubsub.disruptor.core;

import lombok.Data;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
@Data
public class DisruptorBuilder<T> {
	Class<T> t;

}
