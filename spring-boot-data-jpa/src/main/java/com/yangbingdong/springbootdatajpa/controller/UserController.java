package com.yangbingdong.springbootdatajpa.controller;

import com.yangbingdong.springbootdatajpa.domain.repository.UserJpaRepository;
import com.yangbingdong.springbootdatajpa.domain.root.Role;
import com.yangbingdong.springbootdatajpa.domain.root.User;
import com.yangbingdong.springbootdatajpa.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/user")
public class UserController {
	@Autowired
	private UserJpaRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping("/condition/{name}-{email}")
	public List<User> findByCondition(@PathVariable String name, @PathVariable String email) {
		return userService.findByCondition(new User().setName(name).setEmail(email));
	}

	@GetMapping
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping
	public void addNewUser(@Valid @RequestBody User user) {
		userRepository.save(user);
	}

	@GetMapping(path = "/{id}")
	public List<Role> findOne(@PathVariable Long id) {
		return userRepository.findById(id).orElse(null).getRoles();
	}

	@GetMapping(path = "/email/{email}")
	public User findOneByEmail(@PathVariable String email) {
		return userRepository.findFirstByEmail(email);
	}

	@GetMapping(path = "/query1/{name}")
	public List<User> findOneByCustom1(@PathVariable String name) {
		return userRepository.customFindByName(name);
	}

	@DeleteMapping(path = "/{id}")
	public List<User> delete(@PathVariable Long id) {
		return userRepository.deleteByNameStartsWith("yb");
	}

	@GetMapping(value = "/string")
	public String getStringTest() {
		return "{\"success\": true, \"body\": \"这就是我\"}";
	}

	@GetMapping(value = "/error")
	public String getErrorTest() {
		throw new IllegalArgumentException("Error test");
	}

	@GetMapping(path = "/page")
	public Page<User> getAllUserByPage() {
		return userRepository.findByName("ybd", PageRequest.of(1, 1, Sort.Direction.DESC, "createTime"));
	}

	@GetMapping(path = "/sort")
	public Iterable<User> getAllUsersWithSort() {
		return userRepository.findAll(Sort.by(Sort.Direction.ASC, User.NAME));
	}

	@GetMapping(path = "/like")
	public List<User> findUserByNameLike() {
		return userRepository.findByNameStartsWith("yb");
	}
}
