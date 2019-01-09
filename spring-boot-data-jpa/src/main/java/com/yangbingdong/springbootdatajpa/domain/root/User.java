package com.yangbingdong.springbootdatajpa.domain.root;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@Data
@Accessors(chain = true)
@Entity
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "姓名不能为空")
	private String name;
	private String email;

	private LocalDateTime createTime;

	@PrePersist
	protected void prePersist() {
		if (this.createTime == null) {
			createTime = LocalDateTime.now();
		}
	}
}