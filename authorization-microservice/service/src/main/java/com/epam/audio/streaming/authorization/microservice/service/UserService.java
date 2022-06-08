package com.epam.audio.streaming.authorization.microservice.service;

import com.epam.audio.streaming.authorization.microservice.models.User;

import javax.persistence.EntityNotFoundException;

public interface UserService {
    User getUser(String username) throws EntityNotFoundException;
}
