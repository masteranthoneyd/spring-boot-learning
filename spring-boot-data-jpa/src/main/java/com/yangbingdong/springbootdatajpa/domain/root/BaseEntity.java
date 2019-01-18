package com.yangbingdong.springbootdatajpa.domain.root;

import com.yangbingdong.springbootdatajpa.util.SnowflakeIdentifierGenerator;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

/**
 * @author ybd
 * @date 19-1-18
 * @contact yangbingdong1994@gmail.com
 */
@MappedSuperclass
@Data
@Accessors(chain = true)
public abstract class BaseEntity {

	@Id
	@GenericGenerator(name = SnowflakeIdentifierGenerator.NAME, strategy = SnowflakeIdentifierGenerator.CLASS_NAME)
	@GeneratedValue(generator = SnowflakeIdentifierGenerator.NAME)
	protected Long id;

	private LocalDateTime createTime;

	private LocalDateTime updateTime;

	@PrePersist
	protected void prePersist() {
		if (this.createTime == null) {
			createTime = LocalDateTime.now();
		}
	}

}
