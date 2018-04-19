package com.yangbingdong.springbootdatajpa.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author ybd
 * @date 18-3-28
 * @contact yangbingdong1994@gmail.com
 */
@Configuration
public class ValidatorConfiguration {
	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
													  .configure()
													  .failFast(true)
//													  .addProperty( "hibernate.validator.fail_fast", "true" )
													  .buildValidatorFactory();
		return validatorFactory.getValidator();
	}
}
