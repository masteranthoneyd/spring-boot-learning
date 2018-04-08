package com.yangbingdong.docker.filter;

import com.yangbingdong.springbootcommon.utils.IpUtil;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author ybd
 * @date 18-4-8
 * @contact yangbingdong1994@gmail.com
 */
public class IpFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		MDC.put("IP", IpUtil.getIpAddr((HttpServletRequest) request));
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
}
