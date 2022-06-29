package com.epam.audio.streaming.artists.microservice.clients.implimentations;

import com.epam.audio.streaming.artists.microservice.clients.ArtistsApiClient;
import com.epam.audio.streaming.artists.microservice.exceptions.api.artists.ArtistsMicroserviceApiException;
import com.epam.audio.streaming.artists.microservice.models.Artist;
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
public class ArtistsApiClientImpl implements ArtistsApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Artist getArtistById(Long id) throws ArtistsMicroserviceApiException {
        String requestUri = "http://ARTISTS-MICROSERVICE/artists/" + id;
        HttpEntity<String> jwtEntity = JwtEntity.getJwtEntity(requestUri, Arrays.asList("ADMIN"));
        log.info("New jwt entity for 'ALBUMS-MICROSERVICE' has been generated! '{}'", jwtEntity);
        try {
            ResponseEntity<Artist> artistResponseEntity = restTemplate.exchange(requestUri, HttpMethod.GET, jwtEntity, Artist.class);
            log.info("Artist has been got from 'ALBUMS-MICROSERVICE' by id '{}'", id);
            return artistResponseEntity.getBody();
        } catch (RestClientException e) {
            log.error("Artist rest client exception ! {}", e.getMessage());
            throw new ArtistsMicroserviceApiException(String.format("Artist rest client exception ! %s", e.getMessage()));
        }
    }

}
