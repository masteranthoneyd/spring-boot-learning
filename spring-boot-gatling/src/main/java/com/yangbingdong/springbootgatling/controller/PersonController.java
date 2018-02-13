package com.yangbingdong.springbootgatling.controller;

import com.yangbingdong.springbootgatling.model.Person;
import com.yangbingdong.springbootgatling.repository.PersonsRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class PersonController {

	private final PersonsRepository repository;

	@GetMapping("/persons")
	public List<Person> findAll() {
		return (List<Person>) repository.findAll();
	}

	@PostMapping("/persons")
	public Person add(@RequestBody Person person) {
		return repository.save(person);
	}

	@GetMapping("/persons/{id}")
	public Person findById(@PathVariable("id") Long id) {
		return repository.findById(id).orElse(null);
	}

}