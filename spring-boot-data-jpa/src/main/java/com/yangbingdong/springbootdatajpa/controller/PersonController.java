package com.yangbingdong.springbootdatajpa.controller;

import com.yangbingdong.springbootdatajpa.domain.repository.PersonRepository;
import com.yangbingdong.springbootdatajpa.domain.root.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ybd
 * @date 18-3-2
 * @contact yangbingdong1994@gmail.com
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/person")
public class PersonController {
	private final PersonRepository personRepository;

	@GetMapping("/all")
	public List<Person> findAll() {
		return personRepository.findAll();
	}

	@GetMapping("/page")
	public Page<Person> findByPage() {
//		log.info(" \n 分页查询用户："
//				+ " PageNumber = " + pageable.getPageNumber()
//				+ " PageSize = " + pageable.getPageSize());
		Page<Person> page = personRepository.findAll(PageRequest.of(1,3));
		return page;
	}

	@GetMapping("/{id}")
	public Person findById(@PathVariable Long id) {
		return personRepository.getOne(id);
	}

	@GetMapping("/{name}")
	public String add(@PathVariable String name) {
		personRepository.save(new Person().setName(name));
		return "SUCCESS";
	}

}
