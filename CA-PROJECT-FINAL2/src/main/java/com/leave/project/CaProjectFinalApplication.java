package com.leave.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;

@SpringBootApplication(exclude = BatchAutoConfiguration.class)
public class CaProjectFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaProjectFinalApplication.class, args);
	}

}
