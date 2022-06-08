package com.epam.audio.streaming.album.microservice.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "albums")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Min(1900)
    @Max(2022)
    @NotNull(message = "Year is mandatory")
    @Column(name = "year", nullable = false)
    private Integer year;

    @Size(min = 5, max = 2000)
    @Column(name = "notes", length = 2000)
    private String notes;

    @Min(0)
    @NotNull(message = "Artist id is mandatory")
    @Column(name = "artist_id")
    private Long artistId;

    public Album(String name, Integer year, String notes) {
        this.name = name;
        this.year = year;
        this.notes = notes;
    }
}