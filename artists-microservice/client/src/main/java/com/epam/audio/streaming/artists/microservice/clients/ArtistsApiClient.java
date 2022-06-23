package com.epam.audio.streaming.artists.microservice.clients;


import com.epam.audio.streaming.artists.microservice.exceptions.api.MicroserviceApiException;
import com.epam.audio.streaming.artists.microservice.models.Artist;

public interface ArtistsApiClient {
    /**
     * Sends request to the 'ARTISTS-MICROSERVICE' to get artist by selected id.
     * @param id id of artist
     * @return Artist with selected id
     * @throws MicroserviceApiException Trows when microservice not responding or other error was occurred
     */
    Artist getArtistById(Long id) throws MicroserviceApiException;
}
