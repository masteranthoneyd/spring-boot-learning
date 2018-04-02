package com.yangbingdong.springbootdatajpa.exception;

import com.yangbingdong.springbootdatajpa.config.ValidatorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ybd
 * @date 18-3-29
 * @contact yangbingdong1994@gmail.com
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * 由于配置了fast fail {@link ValidatorConfiguration},理论上有且只有一个 ObjectError
	 *
	 * @param bindException
	 * @return
	 */
	@ExceptionHandler(value = {BindException.class})
	@ResponseStatus(HttpStatus.OK)
	public String handle(BindException bindException) {
		String validateFailReason = bindException.getAllErrors()
												 .stream()
												 .findAny()
												 .map(ObjectError::getDefaultMessage)
												 .orElse("");
		log.error("validate fail: field = {}, message = {}", validateFailReason);
		return String.format("validate fail: %s", validateFailReason);
	}
}
