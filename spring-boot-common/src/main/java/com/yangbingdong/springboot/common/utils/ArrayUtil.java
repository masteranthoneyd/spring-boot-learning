package com.yangbingdong.springboot.common.utils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong1994@gmail.com
 */
public final class ArrayUtil {

	public static <T> boolean contains(T[] array, T t) {
		return ArrayUtils.contains(array, t);
	}
}
