package com.epam.audio.streaming.songs.microservice.client.implimentations;

import com.epam.audio.streaming.common.utils.jwt.JwtEntity;
import com.epam.audio.streaming.songs.microservice.client.SongsApiClient;
import com.epam.audio.streaming.songs.microservice.exceptions.api.songs.SongsMicroserviceApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class SongsApiClientImpl implements SongsApiClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Long> deleteSongByAlbumId(Long id) throws SongsMicroserviceApiException {
        String requestUri = "http://SONGS-MICROSERVICE/songs/album/" + id + "/delete";
        HttpEntity<String> jwtEntity = JwtEntity.getJwtEntity(requestUri, Arrays.asList("ADMIN"));
        log.info("New jwt entity for 'SONGS-MICROSERVICE' has been generated! '{}'", jwtEntity);
        try {
            List<Long> idsOfDeletedSongs = (List<Long>) restTemplate.exchange(requestUri, HttpMethod.DELETE, jwtEntity, List.class).getBody();
            log.info("Songs, belongs to the album with id '{}', has been removed from 'SONGS-MICROSERVICE' with ids '{}'", id, idsOfDeletedSongs);
            return idsOfDeletedSongs;
        } catch (RestClientException e) {
            log.error("Songs rest client exception ! {}", e.getMessage());
            throw new SongsMicroserviceApiException(String.format("Songs rest client exception ! %s", e.getMessage()));
        }
    }
}
