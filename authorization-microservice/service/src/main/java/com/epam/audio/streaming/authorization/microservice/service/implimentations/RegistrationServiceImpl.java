package com.epam.audio.streaming.authorization.microservice.service.implimentations;

import com.epam.audio.streaming.authorization.microservice.dao.repositories.RoleRepository;
import com.epam.audio.streaming.authorization.microservice.dao.repositories.UserRepository;
import com.epam.audio.streaming.authorization.microservice.models.Role;
import com.epam.audio.streaming.authorization.microservice.models.User;
import com.epam.audio.streaming.authorization.microservice.service.RegistrationService;
import com.epam.audio.streaming.authorization.microservice.utils.providers.ProviderTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Slf4j
@Service
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
        if (!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {
            Collection<Role> clientRoles = new ArrayList<>();
            clientRoles.add(roleRepository.getByName("USER"));
            user.setRoles(clientRoles);
            user.setEnabled(true);
            log.info("New user '{}' has been registered.", user.getUsername());
            return userRepository.save(user);
        } else {
            log.warn("Username '{}' or email '{}' already exists!",user.getUsername(), user.getEmail());
        }
        return null;
    }

}
