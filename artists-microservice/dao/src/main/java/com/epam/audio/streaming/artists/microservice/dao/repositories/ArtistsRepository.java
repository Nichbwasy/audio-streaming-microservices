package com.epam.audio.streaming.artists.microservice.dao.repositories;

import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ArtistsRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameAndGenresIn(String name, Collection<Genre> genres);
}
