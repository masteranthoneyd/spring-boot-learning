package com.yangbingdong.springbootcommon.utils;

import com.yangbingdong.springbootcommon.utils.function.UncheckedExecution;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong1994@gmail.com
 */
public final class ExecutionTimer {
	public static long timing(UncheckedExecution execution) {
		try {
			long start = System.currentTimeMillis();
			execution.execute();
			long end = System.currentTimeMillis();
			return end - start;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
