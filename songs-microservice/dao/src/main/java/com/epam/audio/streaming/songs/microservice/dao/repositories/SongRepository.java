package com.epam.audio.streaming.songs.microservice.dao.repositories;

import com.epam.audio.streaming.songs.microservice.models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findAllByAlbumId(Long albumId);
    void deleteAllByAlbumId(Long albumId);
}
