package com.epam.audio.streaming.songs.microservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "resources")
public class Resource implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = Storage.class, cascade = CascadeType.ALL)
    private Storage storage;

    @Size(min = 5, max = 512)
    @Column(name = "path", nullable = false, length = 512)
    private String path;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "checksum", nullable = false)
    private String checksum;

    public Resource(Long id, Storage storage, String path, Long size, String checksum) {
        this.id = id;
        this.storage = storage;
        this.path = path;
        this.size = size;
        this.checksum = checksum;
    }

    public Resource(Storage storage, String path, Long size, String checksum) {
        this.storage = storage;
        this.path = path;
        this.size = size;
        this.checksum = checksum;
    }
}
