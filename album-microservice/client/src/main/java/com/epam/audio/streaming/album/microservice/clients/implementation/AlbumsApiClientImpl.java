package com.epam.audio.streaming.album.microservice.clients.implementation;

import com.epam.audio.streaming.album.microservice.clients.AlbumsApiClient;
import com.epam.audio.streaming.album.microservice.exceptions.api.albums.AlbumsMicroserviceApiException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.common.utils.jwt.JwtEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Slf4j
@Component
public class AlbumsApiClientImpl implements AlbumsApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Album getAlbumById(Long id) {
        String requestUri = "http://ALBUMS-MICROSERVICE/albums/" + id;
        HttpEntity<String> jwtEntity = JwtEntity.getJwtEntity(requestUri, Arrays.asList("ADMIN"));
        log.info("New jwt entity for 'ALBUMS-MICROSERVICE' has been generated! '{}'", jwtEntity);
        try {
            ResponseEntity<Album> albumResponseEntity = restTemplate.exchange(requestUri, HttpMethod.GET, jwtEntity, Album.class);
            log.info("Album has bent got from 'ALBUMS-MICROSERVICE' by id '{}'", id);
            return albumResponseEntity.getBody();
        } catch (RestClientException e) {
            log.error("Albums rest client exception ! {}", e.getMessage());
            throw new AlbumsMicroserviceApiException(String.format("Albums rest client exception ! %s", e.getMessage()));
        }
    }


}
