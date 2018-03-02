package com.yangbingdong.springbootdatajpa;

import com.yangbingdong.springbootdatajpa.domain.repository.PersonRepository;
import com.yangbingdong.springbootdatajpa.domain.root.Person;
import com.yangbingdong.springbootdatajpa.domain.vo.Sex;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class SpringBootDataJpaApplication implements CommandLineRunner{
	private final PersonRepository personRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		IntStream.range(0,10)
				 .forEach(this::savePerson);
	}

	@Transactional(rollbackFor = Exception.class)
	public Person savePerson(int i) {
		return personRepository.save(new Person().setName(String.valueOf(i))
												 .setSex(i % 2 == 0 ? Sex.MALE : Sex.FEMALE));
	}
}
