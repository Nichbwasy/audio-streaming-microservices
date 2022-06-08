package com.epam.audio.streaming.songs.microservice.dao.repositories;

import com.epam.audio.streaming.songs.microservice.models.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
