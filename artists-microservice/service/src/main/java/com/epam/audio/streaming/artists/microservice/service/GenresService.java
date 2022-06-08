package com.epam.audio.streaming.artists.microservice.service;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityAlreadyExistsException;
import com.epam.audio.streaming.artists.microservice.models.Genre;

import java.util.List;

public interface GenresService {

    /**
     * Returns all genres from repository.
     * @return List of genres.
     */
    List<Genre> getAllGenres();

    /**
     * Adds a new genre to the repository.
     * @param name Genre name. Must be unique.
     * @return Saved in repository genre.
     * @throws EntityAlreadyExistsException
     */
    Genre addGenre(String name) throws EntityAlreadyExistsException;

    /**
     * Deleted all genres with selected ids from repository
     * @param ids List of genre ids to delete.
     * @return List of genre ids which was deleted.
     */
    List<Long> deleteGenres(List<Long> ids);
}
