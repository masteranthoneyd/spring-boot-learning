package com.yangbingdong.springbootgatling.repository;

import com.yangbingdong.springbootgatling.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonsRepository extends CrudRepository<Person, Long> {
}
