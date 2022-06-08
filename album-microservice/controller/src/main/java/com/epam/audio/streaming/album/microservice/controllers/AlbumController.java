package com.epam.audio.streaming.album.microservice.controllers;

import com.epam.audio.streaming.album.microservice.controllers.dto.AlbumArtistsDTO;
import com.epam.audio.streaming.album.microservice.services.dto.Artist;
import com.epam.audio.streaming.album.microservice.services.utils.JwtEntity;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.EntityValidationException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.album.microservice.services.AlbumsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/albums")
public class AlbumController {

    @Autowired
    private AlbumsService albumsService;

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumsService.getAllAlbums();
        log.info("'{}' albums has been found.", albums.size());
        return ResponseEntity.ok().body(albums);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumArtistsDTO> getAlbum(@PathVariable Long id) throws EntityNotExistsException {
        Album album = albumsService.getAlbum(id);
        Artist artist = albumsService.getAlbumArtist(album);
        AlbumArtistsDTO albumArtistsDTO = new AlbumArtistsDTO(album, artist);
        log.info("Artist with id '{}' has been found.", id);
        return ResponseEntity.ok().body(albumArtistsDTO);
    }

    @PostMapping
    public ResponseEntity<Long> saveAlbum(@Valid @ModelAttribute Album formData) throws EntityValidationException {
        Album album = albumsService.addAlbum(formData);
        log.info("New album '{}' has been saved!", album);
        return ResponseEntity.ok().body(album.getId());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAlbum(@PathVariable Long id, @Valid @ModelAttribute Album formData) throws EntityNotExistsException, EntityValidationException {
        albumsService.updateAlbum(formData);
        log.info("Album with id '{}' has been updated.", id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping
    public ResponseEntity<List<Long>> deleteAlbums(@RequestParam List<Long> id) {
        List<Long> deletedIds = albumsService.deleteAlbums(id);
        return ResponseEntity.ok().body(deletedIds);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Album>> getFilteredAlbums(@RequestParam String name, @RequestParam Integer year) throws EntityValidationException {
        List<Album> albums = albumsService.getFilteredAlbums(name, year);
        log.info("Filtered albums was found.");
        return ResponseEntity.ok().body(albums);
    }

}
