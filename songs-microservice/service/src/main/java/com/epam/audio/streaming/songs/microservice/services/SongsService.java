package com.epam.audio.streaming.songs.microservice.services;

import com.epam.audio.streaming.songs.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.services.dto.Album;

import java.io.InputStream;
import java.util.List;

public interface SongsService {

    /**
     * Saves a new song record in repository and saves song file binary
     * data in resource storage.
     * @param song Song metadata.
     * @param fileData Song binary file data.
     * @return Saved song data.
     */
    Song saveSong(Song song, byte[] fileData);

    /**
     * Gets binary data of song with selected id from resource storage.
     * @param id Id of song to return binary data.
     * @return Binary song data.
     */
    InputStream getSong(Long id);

    /**
     * Gets list of all song's metadata.
     * @return List of all song's metadata.
     */
    List<Song> getAllSongsInfo();

    /**
     * Delete song by id from repository and it binary data from resource storage.
     * @param id Song id to delete.
     */
    void deleteSong(Long id);

    /**
     * Deletes all songs' metadata from repository and its binary data resource storage
     * which belongs to the album with selected id.
     * @param albumId Id of album.
     * @return List of all deleted songs ids.
     */
    List<Long> deleteSongsFromAlbum(Long albumId);

    /**
     * Send request to the ALBUMS_MICROSERVICE to get song album data.
     * @param song Song to get album data.
     * @return Album data.
     * @throws EntityNotExistsException Throws when album with id doesn't exist in albums repository.
     */
    Album getSongAlbum(Song song) throws EntityNotExistsException;

    /**
     * Gets list of songs with the same album.
     * @param albumId Album id.
     * @return List of songs with selected album;
     */
    List<Song> getSongsFromAlbum(Long albumId);
}
