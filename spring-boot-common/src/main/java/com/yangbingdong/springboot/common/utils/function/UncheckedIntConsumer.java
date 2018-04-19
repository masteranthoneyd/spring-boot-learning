package com.yangbingdong.springboot.common.utils.function;

/**
 * @author ybd
 * @date 17-11-28.
 */
@FunctionalInterface
public interface UncheckedIntConsumer {
	void accept(int i) throws Exception;
}
