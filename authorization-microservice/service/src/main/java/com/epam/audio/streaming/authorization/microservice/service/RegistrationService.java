package com.epam.audio.streaming.authorization.microservice.service;

import com.epam.audio.streaming.authorization.microservice.models.User;

public interface RegistrationService {
    User registerUser(User user);
}
