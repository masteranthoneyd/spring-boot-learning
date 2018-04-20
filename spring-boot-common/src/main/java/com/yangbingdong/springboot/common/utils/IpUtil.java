package com.yangbingdong.springboot.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yangbingdong.springboot.common.utils.StringUtil.isNotBlank;

/**
 * @author ybd
 * @date 18-4-8
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
public class IpUtil {
	private static final Pattern PATTERN =Pattern.compile("^(?:(?:[01]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.){3}(?:[01]?\\d{1,2}|2[0-4]\\d|25[0-5])\\b");
	private static final String LOCAL_ADDRESS = "127.0.0.1";
	private static final String LOCAL_ADDRESS2 = "0:0:0:0:0:0:0:1";

	/**
	 * @return 可能有多个，例如：192.168.1.110， 192.168.1.120
	 * @Description: 获取请求中的ip地址：过了多级反向代理，获取的ip不是唯一的，二是包含中间代理层ip。
	 */
	public static String getIp(HttpServletRequest request) {
		Objects.requireNonNull(request);
		String ip = request.getHeader("Cdn-Src-Ip");
		if(isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)){
			return ip;
		}
		ip = request.getHeader("X-Real-IP");
		if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("Proxy-Client-IP");
		if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("WL-Proxy-Client-IP");
		if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("HTTP_CLIENT_IP");
		if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		} else {
			return request.getRemoteAddr();
		}
	}

	public static String realIp(HttpServletRequest request) {
		String ip = getIp(request);
		// 多个路由时，取第一个非unknown的ip
		final String[] arr = ip.split(",");
		for (final String str : arr) {
			if (!"unknown".equalsIgnoreCase(str)) {
				ip = str;
				break;
			}
		}
		return ip;
	}

	/**
	 * 判断是否为ajax请求
	 */
	public static String getRequestType(HttpServletRequest request) {
		return request.getHeader("X-Requested-With");
	}

	/**
	 * 获取服务器IP
	 */
	public static String getServiceIp() {
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					String ip = ips.nextElement().getHostAddress();
					Matcher matcher = PATTERN.matcher(ip);
					if (matcher.matches() && !ip.equals(LOCAL_ADDRESS)) {
						return ip;
					}
				}
			}
		} catch (Exception e) {
			log.error("getServiceIp occurs an error, caused by: ", e);
		}
		return "";
	}
}

