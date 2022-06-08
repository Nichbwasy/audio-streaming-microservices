package com.epam.audio.streaming.songs.microservice.dao.repositories;

import com.epam.audio.streaming.songs.microservice.models.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage getByType(String type);
}
