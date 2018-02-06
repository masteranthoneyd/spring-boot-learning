package com.yangbingdong.springbootcommon.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong@1994.gmail
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
}