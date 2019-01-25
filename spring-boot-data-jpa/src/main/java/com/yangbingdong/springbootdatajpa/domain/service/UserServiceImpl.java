package com.yangbingdong.springbootdatajpa.domain.service;

import com.github.wenhao.jpa.Specifications;
import com.yangbingdong.springbootdatajpa.domain.root.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
@Service
public class UserServiceImpl extends BaseJpaService<User, Long> implements UserService {

	@Override
	public List<User> findByCondition(User condition) {
		Specification<User> spec = Specifications.<User>and()
				.eq(isNotBlank(condition.getName()), User.NAME, condition.getName())
				.eq(isNotBlank(condition.getEmail()), User.EMAIL, condition.getEmail())
				.build();
		return jpaRepository.findAll(spec);
	}
}
