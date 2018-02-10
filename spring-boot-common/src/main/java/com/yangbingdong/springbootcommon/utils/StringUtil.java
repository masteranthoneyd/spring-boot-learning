package com.yangbingdong.springbootcommon.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author ybd
 * @date 18-2-10
 * @contact yangbingdong@1994.gmail
 */
public class StringUtil {

	public static String lowercaseInitial(String s) {
		return StringUtils.uncapitalize(s);
	}

	public static String uppercaseInitial(String s) {
		return StringUtils.capitalize(s);
	}
}
