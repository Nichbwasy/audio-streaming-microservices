package com.epam.audio.streaming.artists.microservice.dao.repositories;

import com.epam.audio.streaming.artists.microservice.models.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenresRepository extends JpaRepository<Genre, Long> {
    Boolean existsByName(String name);
    Genre getByName(String name);
}
