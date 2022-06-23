package com.epam.audio.streaming.songs.microservice.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.epam.audio.streaming.songs.microservice"})
@EntityScan(basePackages = {"com.epam.audio.streaming.songs.microservice"})
@ComponentScan(basePackages = {
        "com.epam.audio.streaming.songs.microservice",
        "com.epam.audio.streaming.album.microservice.clients"
})
public class RunSongsApplication {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(RunSongsApplication.class);
    }
}
