package com.yangbingdong.springbootdatajpa.domain;

import com.yangbingdong.springbootdatajpa.util.SnowflakeIdentifierGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.yangbingdong.springbootdatajpa.domain.SnowflakeIdGenerator.GEN_NAME;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author ybd
 * @date 19-1-9
 * @contact yangbingdong1994@gmail.com
 */
@Target({PACKAGE, TYPE, METHOD, FIELD})
@Retention(RUNTIME)
@GenericGenerator(name = GEN_NAME, strategy = SnowflakeIdentifierGenerator.CLASS_NAME)
public @interface SnowflakeIdGenerator {
	String GEN_NAME = "snowflakeIdentifierGenerator";
}
