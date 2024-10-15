package com.booktestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class BookTestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookTestApiApplication.class, args);
	}

}
