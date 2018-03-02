package com.yangbingdong.springbootdatajpa.domain.repository;

import com.yangbingdong.springbootdatajpa.domain.root.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author ybd
 * @date 18-3-2
 * @contact yangbingdong1994@gmail.com
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
}
