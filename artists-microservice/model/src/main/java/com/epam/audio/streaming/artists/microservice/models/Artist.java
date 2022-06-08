package com.epam.audio.streaming.artists.microservice.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity(name = "artists")
@NoArgsConstructor
@AllArgsConstructor
public class Artist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Size(min = 5, max = 2000)
    @Column(name = "notes", length = 2000)
    private String notes;

    @ToString.Exclude
    @OneToMany(targetEntity = Genre.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Genre> genres;

    public Artist(String name, String notes, List<Genre> genres) {
        this.name = name;
        this.notes = notes;
        this.genres = genres;
    }
}