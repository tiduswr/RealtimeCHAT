package com.tiduswr.rcimageworker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RcImageWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RcImageWorkerApplication.class, args);
	}

}
