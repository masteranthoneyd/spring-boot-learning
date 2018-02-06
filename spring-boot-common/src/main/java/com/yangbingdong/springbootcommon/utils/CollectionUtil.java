package com.yangbingdong.springbootcommon.utils;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong@1994.gmail
 */
public final class CollectionUtil {

	public static <T> boolean isEmpty(Collection<T> collection) {
		return CollectionUtils.isEmpty(collection);
	}

	public static <T> boolean isNotEmpty(Collection<T> collection) {
		return !isEmpty(collection);
	}
}
