package com.yangbingdong.springbootdatajpa.domain.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
public interface JpaService<T, ID> {

	T findById(ID id);

	List<T> findByCondition(T t);

	Page<T> findByCondition(T t, Pageable pageable);
}
