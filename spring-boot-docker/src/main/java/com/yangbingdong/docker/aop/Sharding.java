package com.yangbingdong.docker.aop;

import com.yangbingdong.docker.pubsub.disruptor.core.AbstractDisruptorPublisher;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ybd
 * @date 18-5-7
 * @contact yangbingdong1994@gmail.com
 *
 * Disruptor处理器分片注解
 * 分片逻辑请看: {@link AbstractDisruptorPublisher#resolveDisruptorHandlers}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Sharding {
	int value() default 1;
}
