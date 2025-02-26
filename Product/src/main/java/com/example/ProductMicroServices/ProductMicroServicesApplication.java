package com.example.ProductMicroServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication()
public class ProductMicroServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMicroServicesApplication.class, args);
	}

}
