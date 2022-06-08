package com.epam.audio.streaming.authorization.microservice.service.implimentations;

import com.epam.audio.streaming.authorization.microservice.dao.repositories.UserRepository;
import com.epam.audio.streaming.authorization.microservice.exceptions.user.UserNotFoundException;
import com.epam.audio.streaming.authorization.microservice.models.User;
import com.epam.audio.streaming.authorization.microservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(String username) throws EntityNotFoundException {
        if (userRepository.existsByUsername(username)) {
            return userRepository.getByUsername(username);
        } else {
            log.warn("User with username '{}' not found!", username);
            throw new UserNotFoundException(String.format("User with username '%s' not found!", username));
        }
    }
}
