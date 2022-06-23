package com.epam.audio.streaming.artists.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableEurekaClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.epam.audio.streaming.artists.microservice"})
//Only for a tests!
public class RunArtistsApplication {
    public static void main(String[] args) {
        SpringApplication.run(RunArtistsApplication.class);
    }
}