package com.yangbingdong.springboot.common.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong1994@gmail.com
 */
public class ArrayUtilTest {
	private static String[] collect;

	static {
		collect = IntStream.range(0, 1000000)
						   .parallel()
						   .mapToObj(String::valueOf)
						   .toArray(String[]::new);
	}

	@Test
	public void contains() {
		String b = "b";
		String[] strings = new String[]{"a", b};
		Assertions.assertThat(ArrayUtil.contains(strings, b)).isTrue();
	}

	@Test
	public void countExecuteTime() {
		System.out.println(collect.length);

		System.out.println(ExecutionTimer.timing(() -> System.out.println(Arrays.asList(collect).contains("666666"))));

		System.out.println(ExecutionTimer.timing(() -> ArrayUtil.contains(collect, "666666")));
	}
}