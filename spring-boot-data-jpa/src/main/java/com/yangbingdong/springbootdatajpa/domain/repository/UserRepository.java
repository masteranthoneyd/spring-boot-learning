package com.yangbingdong.springbootdatajpa.domain.repository;

import com.yangbingdong.springbootdatajpa.domain.root.User;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
public interface UserRepository extends CrudRepository<User, Long> {
}
