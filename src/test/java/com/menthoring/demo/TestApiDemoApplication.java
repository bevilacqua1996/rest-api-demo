package com.menthoring.demo;

import org.springframework.boot.SpringApplication;

public class TestApiDemoApplication {

	public static void main(String[] args) {
		SpringApplication.from(ApiDemoApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
