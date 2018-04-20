package com.yangbingdong.docker.domain.core.vo;

import com.yangbingdong.springboot.common.utils.SnowflakeSequencer;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * @author ybd
 * @date 18-3-1
 * @contact yangbingdong1994@gmail.com
 */
public class SnowflakeIdentifierGenerator implements IdentifierGenerator {
	private SnowflakeSequencer snowflakeSequencer = SnowflakeSequencer.INSTANCE;

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return snowflakeSequencer.nextId();
	}
}
