package com.epam.audio.streaming.songs.microservice.services.config;

import com.epam.audio.streaming.songs.microservice.services.SongsService;
import com.epam.audio.streaming.songs.microservice.services.implimentations.SongServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class SongsServiceTestConfiguration {

    @Bean
    public SongsService songsService() {
        return new SongServiceImpl();
    }
}
