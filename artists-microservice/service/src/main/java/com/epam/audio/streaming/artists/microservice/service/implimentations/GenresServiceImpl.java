package com.epam.audio.streaming.artists.microservice.service.implimentations;

import com.epam.audio.streaming.artists.microservice.exceptions.genres.GenreAlreadyExistsException;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.GenresService;
import com.epam.audio.streaming.artists.microservice.dao.repositories.GenresRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class GenresServiceImpl implements GenresService {
    @Autowired
    private GenresRepository genreRepository;

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Genre addGenre(String name) throws GenreAlreadyExistsException {
        if (!genreRepository.existsByName(name)) {
            Genre genre = new Genre(name);
            genre = genreRepository.save(genre);
            if (genre != null) log.info("New genre '{}' has been created.", name);
            else log.warn("Can't create a new genre '{}'!", name);
            return genre;
        } else {
            log.warn("Genre with name '{}' already exists!", name);
            throw new GenreAlreadyExistsException(String.format("Genre with name '%s' already exists!", name));
        }
    }

    @Override
    public List<Long> deleteGenres(List<Long> ids) {
        List<Long> deletedGenresIds = new ArrayList<>();
        ids.forEach(id -> {
            try {
                if (genreRepository.existsById(id)) {
                    genreRepository.deleteById(id);
                    deletedGenresIds.add(id);
                    log.info("Genre with id '{}' has been removed.", id);
                }
            } catch (Exception e) {
                log.error("Something went wrong while deleting genre with id '{}'! {}", id, e.getMessage());
            }
        });
        return deletedGenresIds;
    }
}
