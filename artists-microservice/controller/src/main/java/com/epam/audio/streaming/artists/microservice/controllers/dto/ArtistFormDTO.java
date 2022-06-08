package com.epam.audio.streaming.artists.microservice.controllers.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ArtistFormDTO {
    @Size(min = 3, max = 200)
    private String name;

    @Size(min = 5, max = 2000)
    private String notes;

    private List<Long> genresIds;
}
