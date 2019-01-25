package com.yangbingdong.springbootdatajpa.domain.service.base;

import com.yangbingdong.springbootdatajpa.domain.repository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
public abstract class AbstractJpaService<T, ID> implements JpaService<T,ID> {

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Autowired
	protected BaseJpaRepository<T, ID> jpaRepository;

	@Override
	public T findById(ID id) {
		return jpaRepository.findById(id).orElse(null);
	}

	@Override
	public List<T> findByCondition(T condition) {
		return jpaRepository.findAll(buildSpec(condition));
	}

	@Override
	public Page<T> findByCondition(T condition, Pageable pageable) {
		return jpaRepository.findAll(buildSpec(condition), pageable);
	}

	public abstract Specification<T> buildSpec(T condition);
}
