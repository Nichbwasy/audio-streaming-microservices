package com.epam.audio.streaming.songs.microservice.client.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

@Slf4j
public class JwtEntity {
    public static HttpEntity<String> getJwtEntity(String requestUri, List<String> roles) {
        HttpHeaders headers = getHeaders();
        String tmpToken = getAccessJwtToken(requestUri, roles);
        headers.set("Authorization", "Bearer " + tmpToken);
        return new HttpEntity<>(headers);
    }

    private static String getAccessJwtToken(String requestURL, List<String> authorities){
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = JWT.create()
                .withSubject("SongsService")
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 1000)) //30sec token life time
                .withIssuer(requestURL)
                .withClaim("roles", authorities)
                .sign(algorithm);
        log.info("New authentication token for album-microservice '{}' has been generated.", accessToken);
        return accessToken;
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
