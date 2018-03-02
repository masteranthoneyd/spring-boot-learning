package com.yangbingdong.springbootdatajpa.domain.root;

import com.yangbingdong.springbootdatajpa.domain.vo.Sex;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

import static javax.persistence.EnumType.STRING;

/**
 * @author ybd
 * @date 18-3-2
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
@Slf4j
@Entity
@Table(name = "person")
public class Person {

	@Id
	@GeneratedValue(generator = "snowflakeIdentifierGenerator")
	@GenericGenerator(name = "snowflakeIdentifierGenerator", strategy = "com.yangbingdong.springbootdatajpa.util.SnowflakeIdentifierGenerator")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String name;

	private String address;

	@Enumerated(value = STRING)
	@Column(length = 32)
	private Sex sex;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	private Date createTime;

	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	private Date updateTime;

}
