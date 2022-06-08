package com.epam.audio.streaming.authorization.microservice.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.epam.audio.streaming.authorization.microservice")
@EntityScan(basePackages = "com.epam.audio.streaming.authorization.microservice")
@ComponentScan(basePackages = {"com.epam.audio.streaming.authorization.microservice"})
public class RunAuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunAuthorizationApplication.class);
    }
}
