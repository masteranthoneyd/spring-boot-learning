package com.yangbingdong.springboot.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	public static String getIpAddr(HttpServletRequest request) {
		String ip = LOCAL_ADDRESS;
		if (request != null) {
			ip = request.getHeader("x-forwarded-for");
			if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		}
		return ip;

	}

	/**
	 * @Description: 获取客户端请求中的真实的ip地址
	 * <p>
	 * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
	 * 但是在通过了Apache，Squid等反向代理软件就不能获取到客户端的真实IP地址。而且，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，
	 * 而是一串ip值，例如：192.168.1.110， 192.168.1.120， 192.168.1.130， 192.168.1.100。其中第一个192.168.1.110才是用户真实的ip
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = LOCAL_ADDRESS;
		if (request == null) {
			return ip;
		}
		ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if (ip.equals(LOCAL_ADDRESS) || ip.equals(LOCAL_ADDRESS2)) {
				InetAddress inetAddress;
				try {
					inetAddress = InetAddress.getLocalHost();
					ip = inetAddress.getHostAddress();
				} catch (UnknownHostException e) {
					log.error("getRealIp occurs an error, caused by: ", e);
				}
			}
		}
		//"***.***.***.***".length() = 15
		if (ip != null && ip.length() > 15) {
			int index;
			if ((index = ip.indexOf(StringUtil.COMMA)) > 0) {
				ip = ip.substring(0, index);
			}
		}
		return ip;
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

