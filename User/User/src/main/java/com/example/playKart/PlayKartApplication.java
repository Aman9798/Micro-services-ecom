package com.example.playKart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class PlayKartApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlayKartApplication.class, args);
	}

}
