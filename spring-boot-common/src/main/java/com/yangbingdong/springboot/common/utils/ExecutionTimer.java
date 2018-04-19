package com.yangbingdong.springboot.common.utils;

import com.yangbingdong.springboot.common.utils.function.UncheckedExecution;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author ybd
 * @date 18-2-6
 * @contact yangbingdong1994@gmail.com
 */
public class ExecutionTimer {
	public static Recorder timing(UncheckedExecution execution) {
		try {
			long start = System.currentTimeMillis();
			execution.execute();
			long end = System.currentTimeMillis();
			return new Recorder(end - start);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static class Recorder{
		private long time;
		private double second;

		Recorder(long time) {
			this.time = time;
			this.second = new BigDecimal(String.valueOf(time/1000.00)).setScale(2, RoundingMode.HALF_UP).doubleValue();
		}

		public double getSecond() {
			return second;
		}

		public long getTime() {
			return time;
		}

		@Override
		public String toString() {
			return "Recorder{" +
					"time=" + time +
					", second=" + second +
					'}';
		}
	}
}
