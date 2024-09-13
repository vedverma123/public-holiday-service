package com.cercli.employee.management.publicholidayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PublicHolidayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicHolidayServiceApplication.class, args);
	}

}
