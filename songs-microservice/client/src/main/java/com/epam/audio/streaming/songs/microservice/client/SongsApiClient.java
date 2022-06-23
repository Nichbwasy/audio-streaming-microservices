package com.epam.audio.streaming.songs.microservice.client;

import com.epam.audio.streaming.album.microservice.exceptions.api.MicroserviceApiException;

import java.util.List;

public interface SongsApiClient {
    /**
     * Sends request to the 'SONGS-MICROSERVICE' to delete all songs with selected album.
     * @param id id of album
     * @return List of deleted songs ids
     * @throws MicroserviceApiException Trows when microservice not responding or other error was occurred
     */
    List<Long> deleteSongByAlbumId(Long id) throws MicroserviceApiException;
}
