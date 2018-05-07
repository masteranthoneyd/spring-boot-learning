package com.yangbingdong.springboot.common.utils.disruptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

/**
 * @author ybd
 * @date 18-5-4
 * @contact yangbingdong1994@gmail.com
 */
public interface DisruptorPublisher<S> extends ApplicationListener<ContextClosedEvent>, InitializingBean {
	void publish(S source);
}
