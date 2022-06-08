package com.epam.audio.streaming.artists.microservice.controllers;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityAlreadyExistsException;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.GenresService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenresController {
    @Autowired
    private GenresService genresService;

    @GetMapping
    public ResponseEntity<List<Genre>> getGenres() {
        List<Genre> genres = genresService.getAllGenres();
        log.info("All genres has been returned.");
        return ResponseEntity.ok().body(genres);
    }

    @PostMapping
    public ResponseEntity<Long> saveGenre(@Valid @ModelAttribute Genre genre) throws EntityAlreadyExistsException {
        Genre rez = genresService.addGenre(genre.getName());
        return ResponseEntity.ok().body(rez.getId());
    }

    @DeleteMapping
    public ResponseEntity<List<Long>> deleteGenres(@RequestParam List<Long> id) {
        List<Long> deletedIds = genresService.deleteGenres(id);
        log.info("Genres with ids '{}' were removed", deletedIds);
        return ResponseEntity.ok().body(deletedIds);
    }
}
