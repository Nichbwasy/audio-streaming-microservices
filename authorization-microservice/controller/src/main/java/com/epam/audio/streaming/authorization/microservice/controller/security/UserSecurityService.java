package com.epam.audio.streaming.authorization.microservice.controller.security;

import com.epam.audio.streaming.authorization.microservice.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class UserSecurityService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public com.epam.audio.streaming.authorization.microservice.models.User findUserByUsername(String username) throws UsernameNotFoundException{
        return userRepository.getByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.epam.audio.streaming.authorization.microservice.models.User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User with username '%s' not found!", username));
        }
        return new User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(com.epam.audio.streaming.authorization.microservice.models.User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return authorities;
    }
}
