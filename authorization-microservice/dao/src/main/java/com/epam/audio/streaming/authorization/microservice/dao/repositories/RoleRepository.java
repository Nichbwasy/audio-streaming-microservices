package com.epam.audio.streaming.authorization.microservice.dao.repositories;

import com.epam.audio.streaming.authorization.microservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role getByName(String name);
}
