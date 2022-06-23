package com.epam.audio.streaming.artists.microservice.controllers.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/artists/v2/api-docs/", "/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/artists", "/artists/**", "/genres").hasAnyAuthority("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/artists", "/genres").hasAuthority("ADMIN")
                .antMatchers("/artists/**", "/genres/**").hasAuthority("ADMIN")
                .anyRequest().permitAll().and();
        http.addFilterBefore(new JwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
