package com.satvik.stockpdfspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockPdfSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockPdfSpringBootApplication.class, args);
	}
}
