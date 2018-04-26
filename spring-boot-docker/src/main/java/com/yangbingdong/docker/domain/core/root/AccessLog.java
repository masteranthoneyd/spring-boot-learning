package com.yangbingdong.docker.domain.core.root;

import com.yangbingdong.docker.domain.core.vo.ReqReceiveData;
import com.yangbingdong.docker.domain.core.vo.ReqReceiveDataConverter;
import com.yangbingdong.docker.domain.core.vo.ReqResult;
import com.yangbingdong.docker.pubsub.application.event.PersistAccessLogEvent;
import com.yangbingdong.springboot.common.utils.IpUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static javax.persistence.EnumType.STRING;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

/**
 * @author ybd
 * @date 18-4-20
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
@Slf4j
@Entity
@Table(indexes = {@Index(name = "idx_server_name", columnList = "serverName")})
public class AccessLog implements Serializable {
	public static final String IP = "IP";

	private static final long serialVersionUID = -6911021075718017305L;
	/**
	 * 主键， 仿Snowflake
	 */
	@Id
	@GeneratedValue(generator = "snowflakeIdentifierGenerator")
	@GenericGenerator(name = "snowflakeIdentifierGenerator", strategy = "com.yangbingdong.docker.domain.core.vo.SnowflakeIdentifierGenerator")
	private long id;

	/**
	 * 客户请求时Ip
	 */
	private String clientIp;

	/**
	 * 服务名
	 */
	@Column(length = 50)
	private String serverName;

	/**
	 * 请求URI
	 */
	private String uri;

	/**
	 * 终端请求方式,普通请求,Ajax
	 */
	private String reqType;

	/**
	 * 请求方式method,POST,GET等
	 */
	private String reqMethod;

	/**
	 * 请求的类及方法
	 */
	private String classMethod;

	/**
	 * 请求接收到的参数内容
	 */
	@Column(columnDefinition = "text")
	@Convert(converter = ReqReceiveDataConverter.class)
	private List<ReqReceiveData> reqReceiveDatas;

	/**
	 * 用户token，预留用户信息
	 */
	private String token;

	/**
	 * 接受到请求的时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date reqTime;

	/**
	 * 接口返回数据json
	 */
	private String respData;

	private String httpStatusCode;

	/**
	 * 请求耗时
	 */
	private long timeConsuming;

	/**
	 * 异常描述
	 */
	@Column(columnDefinition = "text")
	private String exceptionMessage;

	/**
	 * 请求开始时间
	 */
	private long startTime;

	/**
	 * 请求结束时间
	 */
	private long endTime;

	/**
	 * 请求成功与否
	 */
	@Enumerated(value = STRING)
	@Column(length = 32)
	private ReqResult reqResult;

	@Transient
	private transient JoinPoint joinPoint;

	@Transient
	private transient ServletRequestAttributes requestAttributes;

	@Transient
	private transient List<Object> domainEvents = new ArrayList<>(16);

	@SuppressWarnings("unused")
	@DomainEvents
	public Collection<Object> domainEvents() {
//		log.info("publish domainEvents......");
		domainEvents.add(new PersistAccessLogEvent(this));
		return Collections.unmodifiableList(domainEvents);
	}

	@SuppressWarnings("unused")
	@AfterDomainEventPublication
	public void callbackMethod() {
//		log.info("AfterDomainEventPublication..........");
		domainEvents.clear();
	}

	public AccessLog parseAndFillReqData() {
		HttpServletRequest request = requestAttributes.getRequest();
		HttpServletResponse response = requestAttributes.getResponse();
		requireNonNull(request);
		requireNonNull(response);
		requireNonNull(joinPoint);
		Signature signature = joinPoint.getSignature();
		this.setClassMethod(signature.getDeclaringTypeName() + "#" + signature.getName())
			.setTimeConsuming(endTime - startTime)
			.setClientIp((String) request.getAttribute(AccessLog.IP))
			.setUri(request.getRequestURL().toString())
			.setReqType(IpUtil.getRequestType(request))
			.setReqMethod(request.getMethod())
			.setToken(UUID.randomUUID().toString())
			.setHttpStatusCode(response.getStatus() + "");
		parseReqReceiveDatas();
		setDefaultValueIfNull();
		return this;
	}

	private void setDefaultValueIfNull() {
		this.exceptionMessage = defaultIfNull(exceptionMessage, "");
		this.respData = defaultIfNull(respData, "");
	}

	private void parseReqReceiveDatas() {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] paramNames = methodSignature.getParameterNames();
		int length = paramNames.length;
		Object[] paramValues = joinPoint.getArgs();
		Class[] paramTypes = methodSignature.getParameterTypes();
		Assert.isTrue(paramValues.length == length, "parse error");
		List<ReqReceiveData> tempReqReceiveDatas = new ArrayList<>(1 << 4);
		for (int i = 0; i < length; i++) {
			if (isJavaxServlet(paramValues[i])) {
				continue;
			}
			tempReqReceiveDatas.add(new ReqReceiveData().setName(paramNames[i])
														.setType(paramTypes[i].getSimpleName())
														.setJsonValue(paramValues[i]));
		}
		this.setReqReceiveDatas(tempReqReceiveDatas);
	}

	public void cleanTransient() {
		this.joinPoint = null;
		this.requestAttributes = null;
	}

	private boolean isJavaxServlet(Object paramValue) {
		return paramValue instanceof HttpServletRequest
				|| paramValue instanceof HttpServletResponse
				|| paramValue instanceof HttpSession;
	}
}
