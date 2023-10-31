package com.adam.companysearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CompanySearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanySearchApplication.class, args);
	}

}
