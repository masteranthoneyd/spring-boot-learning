package com.yangbingdong.springbootcommon.utils;

import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong1994@gmail.com
 */
public final class CollectionUtil {

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> collection) {
		return !isEmpty(collection);
	}

	public static <K, V> Map<K, V> unModify(Map<? extends K, ? extends V> map) {
		return MapUtils.unmodifiableMap(map);
	}

	public static <U, K, V> Map<K, V> enumPoliticMap(U[] u, Function<U, K> f1, Function<U, V> f2) {
		return unModify(Stream.of(u).collect(toMap(f1, f2)));
	}
}
