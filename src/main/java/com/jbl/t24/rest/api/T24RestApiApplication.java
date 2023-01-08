package com.jbl.t24.rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
//import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
//@EnableCircuitBreaker
//@EnableHystrixDashboard
@EnableAsync
public class T24RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(T24RestApiApplication.class, args);
	}
	
	@Bean
//	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
