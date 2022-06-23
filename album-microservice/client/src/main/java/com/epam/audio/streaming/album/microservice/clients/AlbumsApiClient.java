package com.epam.audio.streaming.album.microservice.clients;

import com.epam.audio.streaming.album.microservice.exceptions.api.MicroserviceApiException;
import com.epam.audio.streaming.album.microservice.models.Album;

public interface AlbumsApiClient {
    /**
     * Sends request to the 'ALBUMS-MICROSERVICE' to get artist by selected id.
     * @param id id of album
     * @return Album with selected id
     * @throws MicroserviceApiException Trows when microservice not responding or other error was occurred
     */
    Album getAlbumById(Long id);
}
