package com.yangbingdong.springbootdatajpa.domain.repository;

import com.yangbingdong.springbootdatajpa.domain.root.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@Repository
public interface UserJpaRepository extends BaseJpaRepository<User, Long> {
	Page<User> findByName(String name, Pageable pageable);

	List<User> findByNameStartsWith(String name);

	User findFirstByEmail(String email);

	@Transactional(rollbackFor = Exception.class)
	List<User> deleteByNameStartsWith(String name);

	@Query("select u from #{#entityName} u where u.name like ?1%")
	List<User> customFindByName(String customName);
}
