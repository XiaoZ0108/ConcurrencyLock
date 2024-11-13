package com.example.ConcurrencyLock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class ConcurrencyLockApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrencyLockApplication.class, args);
	}


}
