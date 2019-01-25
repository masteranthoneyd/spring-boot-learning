package com.yangbingdong.springbootdatajpa.domain.service;

import com.yangbingdong.springbootdatajpa.domain.repository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
public class BaseJpaService<T, ID> {

	@Autowired
	protected BaseJpaRepository<T, ID> jpaRepository;
}
