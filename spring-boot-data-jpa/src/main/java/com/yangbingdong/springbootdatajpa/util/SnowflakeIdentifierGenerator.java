package com.yangbingdong.springbootdatajpa.util;

import com.yangbingdong.springboot.common.utils.id.IdGeneratorPlus;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @author ybd
 * @date 18-3-1
 * @contact yangbingdong1994@gmail.com
 */
@SuppressWarnings("unused")
@Slf4j
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {
	public static final String CLASS_NAME = "com.yangbingdong.springbootdatajpa.util.SnowflakeIdentifierGenerator";
	public static final String NAME = "snowflakeIdentifierGenerator";

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return IdGeneratorPlus.shoot();
	}
}
