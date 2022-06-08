package com.epam.audio.streaming.authorization.microservice.dao.repositories;

import com.epam.audio.streaming.authorization.microservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User getByUsername(String username);
}
