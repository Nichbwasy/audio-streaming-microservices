package com.epam.audio.streaming.songs.microservice.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "songs")
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 200)
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Min(1900)
    @NotNull(message = "Year is mandatory")
    @Column(name = "year", nullable = false)
    private Integer year;

    @Size(min = 5, max = 2000)
    @Column(name = "notes", length = 2000)
    private String notes;

    @OneToOne(targetEntity = Resource.class, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Resource resource;

    @Min(0)
    @Column(name = "albumId")
    private Long albumId;

    public Song(String name, Integer year, String notes, Long albumId) {
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.albumId = albumId;
    }

    public Song(String name, Integer year, String notes, Resource resource, Long albumId) {
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.resource = resource;
        this.albumId = albumId;
    }

    public Song(String name, Integer year, String notes, Resource resource) {
        this.name = name;
        this.year = year;
        this.notes = notes;
        this.resource = resource;
    }

    public Song(String name, Integer year, String notes) {
        this.name = name;
        this.year = year;
        this.notes = notes;
    }
}
