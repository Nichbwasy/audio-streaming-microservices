package com.epam.audio.streaming.songs.microservice.services.dto;

import lombok.*;


import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 200)
    private String name;

    @Min(1900)
    @Max(2022)
    @NotNull(message = "Year is mandatory")
    private Integer year;

    @Size(min = 5, max = 2000)
    private String notes;

    @Min(0)
    @NotNull(message = "Artist id is mandatory")
    private Long artistId;

    public Album(String name, Integer year, String notes) {
        this.name = name;
        this.year = year;
        this.notes = notes;
    }
}
