package com.epam.audio.streaming.album.microservice.dao.repositories;

import com.epam.audio.streaming.album.microservice.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByNameAndYear(String name, Integer year);

}
