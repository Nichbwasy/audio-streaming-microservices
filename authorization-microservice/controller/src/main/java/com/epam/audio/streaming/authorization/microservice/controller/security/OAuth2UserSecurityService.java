package com.epam.audio.streaming.authorization.microservice.controller.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.epam.audio.streaming.authorization.microservice.controller.security.custom.CustomOAuth2User;
import com.epam.audio.streaming.authorization.microservice.dao.repositories.UserRepository;
import com.epam.audio.streaming.authorization.microservice.models.User;
import com.epam.audio.streaming.authorization.microservice.service.RegistrationService;
import com.epam.audio.streaming.authorization.microservice.utils.providers.ProviderTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OAuth2UserSecurityService extends DefaultOAuth2UserService {


    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        return new CustomOAuth2User(user);
    }

    public User processOAuthPostLogin(String username, String email) {
        User existUser = userRepository.getByUsername(username);
        if (existUser == null) {
            log.info("Attempt to register new user username='{}', email='{}'.", username, email);
            User user = new User(username, "null", email, ProviderTypes.GOOGLE);
            return registrationService.registerUser(user);
        } else {
            log.info("User with username '{}' and email '{}' has been found.", username, email);
            return existUser;
        }

    }

    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            UserDetails userDetails) throws IOException, ServletException
    {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) //30 min token life time
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        log.info("New authentication token '{}' has been generated.", access_token);
        response.setHeader("access_token", access_token);

    }

}
