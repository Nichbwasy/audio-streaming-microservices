package com.epam.audio.streaming.authorization.microservice.controller.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncoderGenerator {
    public static BCryptPasswordEncoder generateBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }
}
