package com.epam.audio.streaming.authorization.microservice.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class LoginFormDto {
    @Size(min = 3, max = 128)
    private String username;

    @Size(min = 3, max = 64)
    private String password;
}
