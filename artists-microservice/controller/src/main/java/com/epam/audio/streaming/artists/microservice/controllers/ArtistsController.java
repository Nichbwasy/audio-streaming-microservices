package com.epam.audio.streaming.artists.microservice.controllers;

import com.epam.audio.streaming.artists.microservice.controllers.dto.ArtistFormDTO;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.artists.microservice.service.ArtistsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController()
@RequestMapping("/artists")
public class ArtistsController {
    @Autowired
    private ArtistsService artistsService;

    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtist(@PathVariable Long id) throws EntityNotExistsException {
        Artist artist = artistsService.getArtist(id);
        log.info("Artist with id '{}' has been found.", id);
        return ResponseEntity.ok().body(artist);
    }

    @PostMapping
    public ResponseEntity<Long> saveArtist(@Valid @ModelAttribute ArtistFormDTO formData) throws EntityValidationException {
        Artist artist = artistsService.addArtist(formData.getName(), formData.getNotes(), formData.getGenresIds());
        log.info("New artist '{}' has been created." , artist);
        return ResponseEntity.ok().body(artist.getId());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArtist(@PathVariable Long id, @Valid @ModelAttribute ArtistFormDTO formData) throws EntityValidationException, EntityNotExistsException {
        artistsService.updateArtist(id, formData.getName(), formData.getNotes(), formData.getGenresIds());
        log.info("Arist with id '{}' has been updated.", id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping
    public ResponseEntity<List<Long>> deleteArtists(@RequestParam List<Long> id) {
        List<Long> deletedIds = artistsService.deleteArtists(id);
        return ResponseEntity.ok().body(deletedIds);
    }

    @GetMapping
    public ResponseEntity<List<Artist>> getFilteredArtists(@RequestParam String name, @RequestParam List<String> genreNames) {
        List<Artist> artists = artistsService.getFilteredArtists(name, genreNames);
        log.info("Filtered artists was found.");
        return ResponseEntity.ok().body(artists);
    }
}
