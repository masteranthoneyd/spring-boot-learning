package com.yangbingdong.springbootdatajpa.util;

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
	private SnowflakeSequencer snowflakeSequencer = SnowflakeSequencer.INSTANCE;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		Long id = snowflakeSequencer.nextId();
		log.info("SnowflakeIdentifierGenerator... id = {}", id);
		return id;
	}
}
