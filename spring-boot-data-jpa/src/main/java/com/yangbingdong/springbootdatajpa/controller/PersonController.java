package com.yangbingdong.springbootdatajpa.controller;

import com.yangbingdong.springbootdatajpa.domain.repository.PersonRepository;
import com.yangbingdong.springbootdatajpa.domain.root.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	public Flux<Person> findAll() {
		return Flux.fromIterable(personRepository.findAll());
	}

	@GetMapping("/{id}")
	public Mono<Person> findById(@PathVariable Long id) {
		return Mono.just(personRepository.getOne(id));
	}

	@GetMapping("/{name}")
	public Mono<String> add(@PathVariable String name) {
		personRepository.save(new Person().setName(name));
		return Mono.just("SUCCESS");
	}

}
