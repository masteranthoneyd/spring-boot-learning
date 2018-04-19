package com.yangbingdong.springboot.common.utils.location;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ybd
 * @date 18-4-8
 * @contact yangbingdong1994@gmail.com
 */
public class IP2LocationTest {

	private static IP2Location ip2Location;

	@BeforeClass
	public static void setup() {
		ip2Location = new IP2LocationPoll();
		ip2Location.addIP2Location(new IP2LocationXL());
		ip2Location.addIP2Location(new IP2LocationTB());
	}

	@Test
	public void test() {
		String result = ip2Location.ip2Location("192.168.6.113");
		System.out.println(result);
		result = ip2Location.ip2Location("58.63.50.228");
		System.out.println(result);
	}

}