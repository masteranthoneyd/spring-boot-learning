package com.yangbingdong.springbootgatling.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Date;

@Data
@Entity
@SequenceGenerator(name = "seq_person", allocationSize = 1)
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_person")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "birth_date")
	private Date birthDate;

	@Embedded
	private Address address;

}
