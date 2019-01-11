package com.yangbingdong.springbootdatajpa.rest.exception;

import com.yangbingdong.springbootdatajpa.rest.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * @author ybd
 * @date 18-3-29
 * @contact yangbingdong1994@gmail.com
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = {
			MethodArgumentNotValidException.class,
			BindException.class,
			ConstraintViolationException.class})
	@ResponseStatus(HttpStatus.OK)
	public Response<Void> handleValidException(Exception ex) {
		String validateFailReason;
		if (ex instanceof MethodArgumentNotValidException) {
			validateFailReason = ((MethodArgumentNotValidException) ex).getBindingResult()
																	   .getFieldError()
																	   .getDefaultMessage();
		} else if (ex instanceof BindException) {
			validateFailReason = ((BindException) ex).getFieldError().getDefaultMessage();
		} else if (ex instanceof ConstraintViolationException) {
			validateFailReason = ((ConstraintViolationException) ex).getConstraintViolations().stream()
																	.findAny()
																	.map(ConstraintViolation::getMessage)
																	.orElse("Unknown error message");
		} else {
			validateFailReason = "Unknown error message";
		}
		return Response.error(validateFailReason);
	}

	@ExceptionHandler(value = {Exception.class})
	public Response<Void> handle(Exception exception) {
		log.error("GlobalExceptionHandler catch: ", exception);
		return Response.error(exception.getMessage());
	}
}
