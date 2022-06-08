package com.epam.audio.streaming.album.microservice.services.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Artist implements Serializable {
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 200)
    private String name;

    @Size(min = 5, max = 2000)
    private String notes;

    @ToString.Exclude
    private List<Genre> genres;

    public Artist(String name, String notes, List<Genre> genres) {
        this.name = name;
        this.notes = notes;
        this.genres = genres;
    }
}