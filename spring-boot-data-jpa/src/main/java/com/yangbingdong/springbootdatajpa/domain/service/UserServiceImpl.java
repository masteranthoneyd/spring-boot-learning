package com.yangbingdong.springbootdatajpa.domain.service;

import com.github.wenhao.jpa.Specifications;
import com.yangbingdong.springbootdatajpa.domain.root.User;
import com.yangbingdong.springbootdatajpa.domain.service.base.AbstractJpaService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
@Service
public class UserServiceImpl extends AbstractJpaService<User, Long> implements UserService {

	@Override
	public Specification<User> buildSpec(User condition) {
		return Specifications
				.<User>and()
				.eq(isNotBlank(condition.getName()), User.NAME, condition.getName())
				.eq(isNotBlank(condition.getEmail()), User.EMAIL, condition.getEmail())
				.build();
	}
}
