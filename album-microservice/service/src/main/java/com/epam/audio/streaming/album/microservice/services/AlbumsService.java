package com.epam.audio.streaming.album.microservice.services;

import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.EntityValidationException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.artists.microservice.models.Artist;

import java.util.List;

public interface AlbumsService {
    /**
     * Creates a new album in album repository.
     * @param album Album entity to add.
     * @return Saved in repository album.
     * @throws EntityValidationException Throws if album params invalid.
     */
    Album addAlbum(Album album) throws EntityValidationException;

    /**
     * Updated existed album in album repository.
     * @param album Album entity to update.
     * @return Updated album.
     * @throws EntityValidationException Throws when album params invalid.
     * @throws EntityNotExistsException Throws when album not exist in repository.
     */
    Album updateAlbum(Album album) throws EntityValidationException, EntityNotExistsException;

    /**
     * Returns album from repository by id.
     * @param id Id of album to return.
     * @return Album with selected id.
     * @throws EntityNotExistsException Throws when album with selected id not exist in repository.
     */
    Album getAlbum(Long id) throws EntityNotExistsException;

    /**
     * Returns all existed albums in repository.
     * @return List of all albums.
     */
    List<Album> getAllAlbums();

    /**
     * Send request to the ARTISTS-MICROSERVICE to return artist of album.
     * @param album Album to get the artist.
     * @return Artist of the album.
     * @throws EntityNotExistsException Throws when album or artist wasn't found in repository.
     */
    Artist getAlbumArtist(Album album) throws EntityNotExistsException;

    /**
     * Deleted all albums with selected ids from repository
     * @param ids List of album ids to delete.
     * @return List of album ids which was deleted.
     */
    List<Long> deleteAlbums(List<Long> ids);

    /**
     * Returns from repository list of albums filtered by names and year.
     * @param name Name to filter.
     * @param year Year to filter.
     * @return Filtered list of albums.
     */
    List<Album> getFilteredAlbums(String name, Integer year);

}
