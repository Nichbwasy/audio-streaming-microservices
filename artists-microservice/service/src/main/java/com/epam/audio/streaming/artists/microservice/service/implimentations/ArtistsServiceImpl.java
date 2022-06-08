package com.epam.audio.streaming.artists.microservice.service.implimentations;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;
import com.epam.audio.streaming.artists.microservice.exceptions.artists.ArtistGenresValidationException;
import com.epam.audio.streaming.artists.microservice.exceptions.artists.ArtistNotExistException;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.ArtistsService;
import com.epam.audio.streaming.artists.microservice.dao.repositories.ArtistsRepository;
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
public class ArtistsServiceImpl implements ArtistsService {
    @Autowired
    private ArtistsRepository artistRepository;

    @Autowired
    private GenresRepository genreRepository;

    @Override
    public Artist addArtist(String name, String note, List<Long> genres) throws EntityValidationException {
        List<Genre> existedGenres = new ArrayList<>();
        for (Long id : genres) {
            if (genreRepository.existsById(id)) {
                existedGenres.add(genreRepository.getById(id));
            } else {
                log.warn("Genre with id '{}' doesn't exists in storage!", id);
                throw new ArtistGenresValidationException("Required only ids of genres that system already stored");
            }
        }
        Artist artist = new Artist(name, note, existedGenres);
        artist = artistRepository.save(artist);
        if (artist != null) log.info("New artist '{}' has been saved successfully.", artist);
        else log.warn("Can't save a artist!");
        return artist;
    }

    @Override
    public Artist updateArtist(Long updatedArtistId, String name, String note, List<Long> genres) throws EntityNotExistsException, EntityValidationException {
        if (artistRepository.existsById(updatedArtistId)) {
            List<Genre> existedGenres = new ArrayList<>();
            for (Long id : genres) {
                if (genreRepository.existsById(id)) {
                    existedGenres.add(genreRepository.getById(id));
                } else {
                    log.warn("Genre with id '{}' doesn't exists in storage!", id);
                    throw new ArtistGenresValidationException("Required only ids of genres that system already stored");
                }
            }
            Artist artist = new Artist(updatedArtistId, name, note, existedGenres);
            artist = artistRepository.save(artist);
            if (artist != null) log.info("New artist '{}' has been saved successfully.", artist);
            else log.warn("Can't save a artist!");
            return artist;
        } else {
            log.warn("Artist with id '{}' doesn't exists!", updatedArtistId);
            throw new ArtistNotExistException("Required only ids of artists that system already stored");
        }
    }

    @Override
    public Artist getArtist(Long id) throws EntityNotExistsException {
        if (artistRepository.existsById(id)) {
            return artistRepository.findById(id).get();
        } else {
            log.warn("Artist with id '{}' doesn't exists!", id);
            throw new ArtistNotExistException("Required only ids of artists that system already stored");
        }
    }

    @Override
    public List<Long> deleteArtists(List<Long> ids) {
        List<Long> deletedArtistsId = new ArrayList<>();
        ids.forEach(id -> {
            try {
                if (artistRepository.existsById(id)) {
                    artistRepository.deleteById(id);
                    deletedArtistsId.add(id);
                    log.info("Genre with id '{}' has been removed.", id);
                } else {
                    log.warn("Artist with id '{}' doesn't exists!", id);
                    throw new ArtistNotExistException("Required only ids of artists that system already stored");
                }
            } catch (Exception e) {
                log.warn("Something went wrong while deleting genre with id '{}'! {}", id, e.getMessage());
            }
        });
        return deletedArtistsId;
    }

    @Override
    public List<Artist> getFilteredArtists(String name, List<String> genres) {
        List<Genre> existedGenres = new ArrayList<>();
        genres.forEach(g -> {
            if (genreRepository.existsByName(g))
                existedGenres.add(genreRepository.getByName(g));
        });
        List<Artist> artists = artistRepository.findByNameAndGenresIn(name, existedGenres);
        if (artists.size() > 0) log.info("Artists with name '{}' or genres '{}' was found!", name, genres);
        else log.warn("Can't find any artist with name '{}' or genres '{}' was found!", name, genres);
        return artists;
    }
}
