package com.yangbingdong.springboot.common.utils.id;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * @author ybd
 * @date 19-1-9
 * @contact yangbingdong1994@gmail.com
 */
public class IdGeneratorPlus {
	private static final Snowflake[] SNOWFLAKES = IntStream.rangeClosed(1, 8)
														   .mapToObj(Snowflake::create)
														   .toArray(Snowflake[]::new);

	private static final AtomicLong ATOMIC_LONG = new AtomicLong(0);

	public static long shoot() {
		return SNOWFLAKES[(int) (ATOMIC_LONG.incrementAndGet() & (1 << 3) - 1)].nextId();
	}
}
