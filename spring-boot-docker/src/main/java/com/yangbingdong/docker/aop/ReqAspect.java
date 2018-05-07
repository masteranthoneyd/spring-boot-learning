package com.yangbingdong.docker.aop;

import com.alibaba.fastjson.JSON;
import com.yangbingdong.docker.domain.core.root.AccessLog;
import com.yangbingdong.docker.domain.core.vo.ReqResult;
import com.yangbingdong.springboot.common.utils.disruptor.DisruptorPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * @author ybd
 * @date 18-4-20
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class ReqAspect {
	private final DisruptorPublisher<AccessLog> publisher;

	@Value("${spring.application.name:}")
	private String serverName;

	private ThreadLocal<AccessLog> logThreadLocal = new ThreadLocal<>();

	@Pointcut("execution(public * com.yangbingdong.docker.controller..*.*(..))")
	public void path() {
	}

	@Pointcut("@annotation(ReqLog)")
	public void annotation() {
	}

	@Pointcut("path() && annotation()")
	public void webLog() {
	}

	@Before("webLog() && @annotation(reqLog)")
	public void doBeforeAdvice(JoinPoint joinPoint, ReqLog reqLog) {
		long currentTimeMillis = System.currentTimeMillis();
		Date currentDate = new Date(currentTimeMillis);
		AccessLog accessLog = new AccessLog().setJoinPoint(joinPoint)
											 .setServerName(serverName)
											 .setReqTime(currentDate)
											 .setStartTime(currentTimeMillis);
		logThreadLocal.set(accessLog);
	}

	/**
	 * 这里需要注意的是:
	 * 如果参数中的第一个参数为JoinPoint，则第二个参数为返回值的信息
	 * 如果参数中的第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
	 * returning 限定了只有目标方法返回值与通知方法相应参数类型时才能执行后置返回通知，否则不执行，对于returning对应的通知方法参数为Object类型将匹配任何目标返回值
	 */
	@AfterReturning(value = "webLog()", returning = "respData")
	public void doAfterReturningAdvice(Object respData) {
		try {
			AccessLog accessLog = logThreadLocal.get();
			accessLog.setEndTime(System.currentTimeMillis())
					 .setRespData(JSON.toJSONString(respData))
					 .setReqResult(ReqResult.SUCCESS);
			sentAccessLogEvent(accessLog);
		} finally {
			logThreadLocal.remove();
		}
	}

	/**
	 * 定义一个名字，该名字用于匹配通知实现方法的一个参数名，当目标方法抛出异常返回后，将把目标方法抛出的异常传给通知方法；
	 * throwing 限定了只有目标方法抛出的异常与通知方法相应参数异常类型时才能执行后置异常通知，否则不执行，
	 * 对于throwing对应的通知方法参数为Throwable类型将匹配任何异常。
	 */
	@AfterThrowing(value = "webLog()", throwing = "exception")
	public void doAfterThrowingAdvice(Throwable exception) {
		try {
			AccessLog accessLog = logThreadLocal.get();
			accessLog.setEndTime(System.currentTimeMillis())
					 .setExceptionMessage(exception.getMessage())
					 .setReqResult(ReqResult.FAIL);
			sentAccessLogEvent(accessLog);
		} finally {
			logThreadLocal.remove();
		}

	}

	private void sentAccessLogEvent(AccessLog accessLog) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		accessLog.setRequestAttributes(requestAttributes);
		publisher.publish(accessLog);
	}

}
