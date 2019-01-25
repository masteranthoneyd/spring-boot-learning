package com.yangbingdong.springbootdatajpa.controller;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ybd
 * @date 18-5-15
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
public class Response<T> {
	private boolean success = true;
	private T body;
	private String errorMsg;

	@SuppressWarnings("unchecked")
	public static <T> Response<T> ok(T body) {
		return body instanceof Response ? (Response<T>) body : new Response<T>().setBody(body);
	}

	public static Response<Void> ok() {
		return new Response<>();
	}

	public static Response<Void> error(Exception e) {
		return new Response<Void>().setSuccess(false).setErrorMsg(e.getMessage());
	}

	public static Response<Void> error(String errorMsg) {
		return new Response<Void>().setSuccess(false).setErrorMsg(errorMsg);
	}
}
