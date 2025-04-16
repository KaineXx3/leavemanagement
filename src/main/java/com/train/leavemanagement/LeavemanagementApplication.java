package com.train.leavemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LeavemanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeavemanagementApplication.class, args);
	}

}
