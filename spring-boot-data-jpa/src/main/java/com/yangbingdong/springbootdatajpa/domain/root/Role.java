package com.yangbingdong.springbootdatajpa.domain.root;

import com.yangbingdong.springbootdatajpa.util.SnowflakeIdentifierGenerator;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author ybd
 * @date 19-1-21
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
@Entity
public class Role {

	@Id
	@GenericGenerator(name = SnowflakeIdentifierGenerator.NAME, strategy = SnowflakeIdentifierGenerator.CLASS_NAME)
	@GeneratedValue(generator = SnowflakeIdentifierGenerator.NAME)
	private Long id;

	private String name;
}
