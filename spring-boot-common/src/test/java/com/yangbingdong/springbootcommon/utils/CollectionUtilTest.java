package com.yangbingdong.springbootcommon.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong1994@gmail.com
 */
public class CollectionUtilTest {
	private static List<Integer> collect;

	static {
		collect = IntStream.range(0, 10000)
						   .parallel()
						   .boxed()
						   .collect(toList());
	}

	@Test
	public void isEmpty() {
		Assertions.assertThat(CollectionUtil.isEmpty(collect)).isFalse();
	}

	@Test
	public void isNotEmpty() {
		Assertions.assertThat(CollectionUtil.isNotEmpty(collect)).isTrue();
	}

	@Test
	public void unModify() {
		Map<Integer, List<String>> map = new HashMap<>();
		map.put(1, Collections.singletonList("A"));
		map = CollectionUtil.unModify(map);

		Map<Integer, List<String>> finalMap = map;
		Assertions.assertThatThrownBy(() -> finalMap.put(2, Collections.singletonList("A")))
				  .isInstanceOf(UnsupportedOperationException.class);

	}
}