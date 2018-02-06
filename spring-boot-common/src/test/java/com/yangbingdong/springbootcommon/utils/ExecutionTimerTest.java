package com.yangbingdong.springbootcommon.utils;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong@1994.gmail
 */
public class ExecutionTimerTest {

	@Test
	public void timing() throws Exception {
		long exeTime = ExecutionTimer.timing(() -> TimeUnit.SECONDS.sleep(1));
		System.out.println(exeTime);
	}
}