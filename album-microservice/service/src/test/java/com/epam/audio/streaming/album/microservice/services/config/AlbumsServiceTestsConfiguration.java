package com.epam.audio.streaming.album.microservice.services.config;

import com.epam.audio.streaming.album.microservice.services.AlbumsService;
import com.epam.audio.streaming.album.microservice.services.implimentations.AlbumsServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class AlbumsServiceTestsConfiguration {

    @Bean
    public AlbumsService albumsService() {
        return new AlbumsServiceImpl();
    }

}
