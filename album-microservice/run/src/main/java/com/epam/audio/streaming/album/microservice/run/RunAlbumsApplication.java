package com.epam.audio.streaming.album.microservice.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.epam.audio.streaming.album.microservice")
@EntityScan(basePackages = "com.epam.audio.streaming.album.microservice")
@ComponentScan(basePackages = {
        "com.epam.audio.streaming.album.microservice",
        "com.epam.audio.streaming.artists.microservice.clients",
        "com.epam.audio.streaming.songs.microservice.client"
})
public class RunAlbumsApplication {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    public static void main(String[] args) {
        SpringApplication.run(RunAlbumsApplication.class);
    }
}