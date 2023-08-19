package com.tiduswr.rcauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RcAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(RcAuthApplication.class, args);
	}

}
