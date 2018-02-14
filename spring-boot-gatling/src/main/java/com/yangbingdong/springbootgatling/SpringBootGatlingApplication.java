package com.yangbingdong.springbootgatling;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication
public class SpringBootGatlingApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootGatlingApplication.class, args);
	}

	@Resource
	DataSource dataSource;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("DATASOURCE = " + dataSource);
	}
}
