package com.epam.audio.streaming.album.microservice.services.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Genre implements Serializable {

    private Long id;

    @Size(min = 3, max = 128)
    @NotBlank(message = "Name is mandatory")
    private String name;

    public Genre(String name) {
        this.name = name;
    }
}