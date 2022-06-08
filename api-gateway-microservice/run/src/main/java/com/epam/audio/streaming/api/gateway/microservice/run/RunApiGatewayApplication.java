package com.epam.audio.streaming.api.gateway.microservice.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.epam.audio.streaming.api.gateway.microservice"})
public class RunApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunApiGatewayApplication.class);
    }
}
