package com.yangbingdong.springbootdatajpa.domain.repository;

import com.yangbingdong.springbootdatajpa.domain.root.Role;
import org.springframework.stereotype.Repository;

/**
 * @author ybd
 * @date 19-1-21
 * @contact yangbingdong1994@gmail.com
 */
@Repository
public interface RoleJpaRepository extends BaseJpaRepository<Role, Integer> {
}
