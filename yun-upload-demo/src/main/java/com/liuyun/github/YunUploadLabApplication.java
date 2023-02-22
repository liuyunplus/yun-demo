package com.liuyun.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class YunUploadLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(YunUploadLabApplication.class, args);
	}

}
