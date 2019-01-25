package com.yangbingdong.springbootdatajpa.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
@NoRepositoryBean
public interface BaseJpaRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
