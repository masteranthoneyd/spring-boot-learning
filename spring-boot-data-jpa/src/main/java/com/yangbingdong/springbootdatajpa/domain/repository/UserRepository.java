package com.yangbingdong.springbootdatajpa.domain.repository;

import com.yangbingdong.springbootdatajpa.domain.root.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
public interface UserRepository extends JpaRepository<User, Long> {
	Page<User> findByName(String name, Pageable pageable);

	List<User> findByNameStartsWith(String name);

	@Transactional(rollbackFor = Exception.class)
	List<User> deleteByNameStartsWith(String name);
}
