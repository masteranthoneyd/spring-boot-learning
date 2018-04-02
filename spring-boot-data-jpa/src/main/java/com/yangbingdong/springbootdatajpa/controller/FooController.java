package com.yangbingdong.springbootdatajpa.controller;

import com.yangbingdong.springbootdatajpa.domain.root.Foo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author ybd
 * @date 18-3-28
 * @contact yangbingdong1994@gmail.com
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class FooController {

	private final Validator globalValidator;

	@PostMapping("/foo")
	public String foo(@Validated Foo foo, BindingResult bindingResult) {
		log.info("foo: {}", foo);
		if (bindingResult.hasErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error("valid fail: field = {}, message = {}", fieldError.getField(), fieldError.getDefaultMessage());
			}
			return "fail";
		}
		return "success";
	}

	@PostMapping("/foo1")
	public String foo1(@Validated Foo foo) {
		log.info("foo: {}", foo);
		return "success";
	}

	@PostMapping("/foo2")
	public String foo2(Foo foo) {
		log.info("foo2: {}", foo);
		Set<ConstraintViolation<Foo>> set = globalValidator.validate(foo);
		for (ConstraintViolation<Foo> constraintViolation : set) {
			System.out.println("vvvvvvvvv: " + constraintViolation.getMessage());
		}
		return "success";
	}
}
