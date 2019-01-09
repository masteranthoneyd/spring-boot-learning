package com.yangbingdong.springbootdatajpa.rest;

import com.yangbingdong.springbootdatajpa.domain.repository.UserRepository;
import com.yangbingdong.springbootdatajpa.domain.root.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author ybd
 * @date 19-1-8
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/user")
public class UserController {
	@Autowired
	private UserRepository userRepository;

	@GetMapping
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping
	public void addNewUser(@Valid @RequestBody User user) {
		userRepository.save(user);
	}

	@GetMapping(path = "/{id}")
	public User findOne(@PathVariable Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@DeleteMapping(path = "/{id}")
	public void delete(@PathVariable Long id) {
		userRepository.deleteById(id);
	}

	@GetMapping(value = "/string")
	public String getStringTest() {
		return "{\"success\": true, \"body\": \"这就是我\"}";
	}

	@GetMapping(value = "/error")
	public String getErrorTest() {
		throw new IllegalArgumentException("Error test");
	}
}
