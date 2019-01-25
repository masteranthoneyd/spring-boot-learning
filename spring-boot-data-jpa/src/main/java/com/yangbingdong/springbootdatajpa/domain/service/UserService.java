package com.yangbingdong.springbootdatajpa.domain.service;

import com.yangbingdong.springbootdatajpa.domain.root.User;

import java.util.List;

/**
 * @author ybd
 * @date 19-1-25
 * @contact yangbingdong1994@gmail.com
 */
public interface UserService {
	List<User> findByCondition(User condition);
}
