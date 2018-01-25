package com.yangbingdong.springbootgatling.repository;

import com.yangbingdong.springbootgatling.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonsRepository extends CrudRepository<Person, Long> {
}
