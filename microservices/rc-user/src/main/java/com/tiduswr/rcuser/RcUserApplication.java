package com.tiduswr.rcuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RcUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(RcUserApplication.class, args);
	}

}
