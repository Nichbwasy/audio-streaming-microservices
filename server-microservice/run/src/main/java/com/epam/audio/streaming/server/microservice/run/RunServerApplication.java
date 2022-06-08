package com.epam.audio.streaming.server.microservice.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaServer
@SpringBootApplication
@EntityScan(basePackages = "com.epam.audio.streaming.server.microservice")
@ComponentScan(basePackages = {"com.epam.audio.streaming.server.microservice"})
public class RunServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunServerApplication.class);
    }
}
