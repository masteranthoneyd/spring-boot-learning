package com.yangbingdong.springbootcommon.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(CollectionUtil.isEmpty(collect)).isFalse();
	}

	@Test
	public void isNotEmpty() {
		assertThat(CollectionUtil.isNotEmpty(collect)).isTrue();
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

	@Test
	public void enumPoliticMap() {
		assertThat(Handlers.MAP)
				.containsOnlyKeys(Stream.of(Handlers.values())
										.map(Handlers::getIndex)
										.toArray(Integer[]::new));

	}

	@AllArgsConstructor
	@Getter
	enum Handlers {
		FIRST_HANDLER(0, new Object()),
		SECOND_HANDLER(1, new Object());

		public static final Map<Integer, Object> MAP;

		static {
			MAP = CollectionUtil.enumPoliticMap(Handlers.values(), Handlers::getIndex, Handlers::getHandler);
		}

		private int index;
		private Object handler;
	}
}