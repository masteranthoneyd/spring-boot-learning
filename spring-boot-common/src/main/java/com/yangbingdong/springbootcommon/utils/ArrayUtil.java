package com.yangbingdong.springbootcommon.utils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong@1994.gmail
 */
public final class ArrayUtil {

	public static <T> boolean contains(T[] array, T t) {
		return ArrayUtils.contains(array, t);
	}
}
