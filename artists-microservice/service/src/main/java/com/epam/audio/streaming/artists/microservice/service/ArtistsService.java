package com.epam.audio.streaming.artists.microservice.service;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;
import com.epam.audio.streaming.artists.microservice.exceptions.artists.ArtistNotExistException;
import com.epam.audio.streaming.artists.microservice.models.Artist;

import java.util.List;

public interface ArtistsService {

    /**
     * Adds a new artist to the repository.
     * @param name Artist name.
     * @param note Notes about artist.
     * @param genres List of genres ids. All genres must exist.
     * @return Saved in repository artist.
     * @throws EntityValidationException Throws when arist params invalid.
     */
    Artist addArtist(String name, String note, List<Long> genres) throws EntityValidationException;

    /**
     * Updates an existed artist in repository.
     * @param updatedArtistId Artist id.
     * @param name New artist name.
     * @param note New notes about artist.
     * @param genres New genres ids for artist.
     * @return Updated artist.
     * @throws EntityNotExistsException Throws when arist with id doesn't exist in repository.
     * @throws EntityValidationException Throws when arist params invalid.
     */
    Artist updateArtist(Long updatedArtistId, String name, String note, List<Long> genres) throws EntityNotExistsException, EntityValidationException;

    /**
     * Returns album by id.
     * @param id Artist id to get.
     * @return Artist with id.
     * @throws EntityNotExistsException Throws when arist with id doesn't exist in repository.
     */
    Artist getArtist(Long id) throws EntityNotExistsException;

    /**
     * Delete all existed artist from repository by ids.
     * @param ids List of ids of genres to delete.
     * @return List genres ids which was deleted.
     */
    List<Long> deleteArtists(List<Long> ids);

    /**
     * Returns list of artists filtered by name and genres.
     * @param name Artist name to filter.
     * @param genres List of genres to filter.
     * @return List od filtered artists.
     */
    List<Artist> getFilteredArtists(String name, List<String> genres);
}
