package com.yangbingdong.springboot.common.utils.function;

/**
 * @author ybd
 * @date 19-1-10
 * @contact yangbingdong1994@gmail.com
 */
public interface UncheckedBiFunction<T, U, R> {
	R apply(T t, U u) throws Exception;
}
