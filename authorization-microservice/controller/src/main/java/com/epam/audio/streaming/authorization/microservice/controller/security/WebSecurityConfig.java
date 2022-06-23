package com.epam.audio.streaming.authorization.microservice.controller.security;

import com.epam.audio.streaming.authorization.microservice.controller.security.custom.CustomOAuth2User;
import com.epam.audio.streaming.authorization.microservice.controller.security.utils.EncoderGenerator;
import com.epam.audio.streaming.authorization.microservice.dao.repositories.UserRepository;
import com.epam.audio.streaming.authorization.microservice.models.User;
import com.epam.audio.streaming.authorization.microservice.utils.roles.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static List<String> clients = Arrays.asList("google");

    @Autowired
    private OAuth2UserSecurityService oAuth2UserSecurityService;

    @Autowired
    private UserSecurityService securityService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/", "/login", "/register", "/test", "/oauth/**", "/v2/api-docs/**", "/swagger-ui.html").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin().permitAll()
                .defaultSuccessUrl("/", true)
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                    .userInfoEndpoint()
                          .userService(oAuth2UserSecurityService)
                .and()
                .successHandler((request, response, authentication) -> {

                    CustomOAuth2User oauthUser = new CustomOAuth2User((OAuth2AuthenticatedPrincipal) authentication.getPrincipal());

                    User user = oAuth2UserSecurityService.processOAuthPostLogin(oauthUser.getName(), oauthUser.getEmail());
                    UserDetails userDetails = securityService.loadUserByUsername(user.getUsername());
                    oAuth2UserSecurityService.successfulAuthentication(request, response, userDetails);

                });
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean(), securityService));
        http.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return EncoderGenerator.generateBCryptEncoder();
    }
}
