package com.yangbingdong.springbootgatling.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {

	private String country;

	private String city;

	@Column(name = "postal_code")
	private String postalCode;

	private String street;

	@Column(name = "house_no")
	private int houseNo;

	@Column(name = "flat_no")
	private int flatNo;
}
