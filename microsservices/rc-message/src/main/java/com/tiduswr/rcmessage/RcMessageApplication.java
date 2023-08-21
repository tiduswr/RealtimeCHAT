package com.tiduswr.rcmessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableFeignClients
public class RcMessageApplication {

	public static void main(String[] args) {
		SpringApplication.run(RcMessageApplication.class, args);
	}

}
