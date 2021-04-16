package com.caseconquer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.caseconquer.controllers"})
@EntityScan(basePackages = {"com.caseconquer.models"})

public class CaseconquerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaseconquerApplication.class, args);
	}
}
