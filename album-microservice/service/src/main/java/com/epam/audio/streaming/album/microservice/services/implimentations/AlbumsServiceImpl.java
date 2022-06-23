package com.epam.audio.streaming.album.microservice.services.implimentations;

import com.epam.audio.streaming.album.microservice.dao.repositories.AlbumRepository;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.album.AlbumNotExistsException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.album.microservice.services.AlbumsService;
import com.epam.audio.streaming.artists.microservice.clients.ArtistsApiClient;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.songs.microservice.client.SongsApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AlbumsServiceImpl implements AlbumsService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistsApiClient artistsApiClient;

    @Autowired
    private SongsApiClient songsApiClient;

    @Override
    public Album addAlbum(Album album) throws EntityNotExistsException {
        if (getAlbumArtist(album) != null) {
            album = albumRepository.save(album);
            if (album != null) log.info("New album has been saved.");
            else log.warn("New album wasn't saved.");
            return album;
        } else {
            log.warn("Artist with id '{}' not found in artists-microservice!", album.getArtistId());
            throw new EntityNotExistsException(String.format("Artist with id '%d' not found in artists-microservice!", album.getArtistId()));
        }
    }

    @Override
    public Album updateAlbum(Album album) throws AlbumNotExistsException {
        if (albumRepository.existsById(album.getId())) {
            if (getAlbumArtist(album) != null) {
                album = albumRepository.save(album);
                if (album != null) log.info("New album has been saved.");
                else log.warn("New album wasn't saved.");
                return album;
            } else {
                log.warn("Artist with id '{}' not found in artists-microservice!", album.getArtistId());
                throw new EntityNotExistsException(String.format("Artist with id '%d' not found in artists-microservice!", album.getArtistId()));
            }
        } else {
            log.warn("Album doesn't exist with given id '{}'!", album.getId());
            throw new AlbumNotExistsException("Album doesn't exist with given id");
        }
    }

    @Override
    public Album getAlbum(Long id) throws AlbumNotExistsException {
        if (albumRepository.existsById(id)) {
            return albumRepository.findById(id).get();
        } else {
            log.warn("Album doesn't exist with given id '{}'!", id);
            throw new AlbumNotExistsException(String.format("Album doesn't exist with given id '%d'!", id));
        }
    }

    @Override
    public List<Album> getAllAlbums() {
        log.info("Returning all albums...");
        List<Album> albums = albumRepository.findAll();
        return albums;
    }

    @Override
    public List<Long> deleteAlbums(List<Long> ids) {
        List<Long> deletedAlbumIds = new ArrayList<>();
        ids.forEach(id -> {
            try {
                if (albumRepository.existsById(id)) {
                    log.info("Deleting all songs belongs to album with id '{}'...", id);
                    List<Long> idsOfDeletedSongs = songsApiClient.deleteSongByAlbumId(id);
                    log.info("Songs with ids '{}' has been removed.", idsOfDeletedSongs);

                    albumRepository.deleteById(id);
                    deletedAlbumIds.add(id);
                    log.info("Album with id '{}' has been removed.", id);
                } else {
                    log.warn("Album with id '{}' doesn't exists!", id);
                    throw new AlbumNotExistsException("Required only ids of albums that system already stored");
                }
            } catch (Exception e) {
                log.warn("Something went wrong while deleting album with id '{}'! {}", id, e.getMessage());
            }
        });
        return deletedAlbumIds;
    }

    @Override
    public Artist getAlbumArtist(Album album) throws EntityNotExistsException {
        Artist artist = artistsApiClient.getArtistById(album.getArtistId());
        if (artist != null) {
            log.info("Artist has been received from artist-microservice.");
            return artist;
        }
        else {
            log.warn("Can't get artist with id '{}' from artist-microservice!", album.getArtistId());
            throw new EntityNotExistsException(String.format("Can't get artist with id '%s' from artist-microservice!", album.getArtistId()));
        }
    }

    @Override
    public List<Album> getFilteredAlbums(String name, Integer year) {
        List<Album> albums = albumRepository.findByNameAndYear(name, year);
        if (albums != null) log.info("Albums was found!");
        else log.warn("Can't find any album!");
        return albums;
    }
}
