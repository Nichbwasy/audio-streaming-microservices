package com.epam.audio.streaming.songs.microservice.services.implimentations;

import com.epam.audio.streaming.album.microservice.clients.AlbumsApiClient;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.songs.microservice.dao.repositories.SongRepository;
import com.epam.audio.streaming.songs.microservice.dao.repositories.StorageRepository;
import com.epam.audio.streaming.songs.microservice.dao.storages.ResourceStorage;
import com.epam.audio.streaming.songs.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3NotFoundException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3ValidationException;
import com.epam.audio.streaming.songs.microservice.models.Resource;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.models.Storage;
import com.epam.audio.streaming.songs.microservice.services.SongsService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class SongServiceImpl implements SongsService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private ResourceStorage resourceStorage;

    @Autowired
    private AlbumsApiClient albumsApiClient;

    @SneakyThrows
    @Override
    public Song saveSong(Song song, byte[] fileData) {
        Tika tika = new Tika();
        InputStream data = new ByteArrayInputStream(fileData);
        if (tika.detect(data).equals("audio/mpeg")){
            log.info("File '{}' is valid.", song.getName());
            if (getSongAlbum(song) != null) {
                Storage storage = storageRepository.getByType("RESOURCE_STORAGE");
                Resource resource = new Resource(storage, "some_path", 1L, "none");
                song.setResource(resource);
                resourceStorage.saveFile(song.getName(), data);
                data.close();
                return songRepository.save(song);
            } else {
                log.warn("Song album with id '{}' not found in albums-microservice!", song.getAlbumId());
                throw new EntityNotExistsException(String.format("Song album with id '%d' not found in albums-microservice!", song.getAlbumId()));
            }
        } else {
            log.info("File '{}' isn't valid!", song.getName());
            throw new Mp3ValidationException("Not valid file! ");
        }
    }

    @Override
    public InputStream getSong(Long id){
        if (songRepository.existsById(id)) {
            Song song = songRepository.getById(id);
            String songName = song.getName();
            return resourceStorage.getFileByName(songName);
        } else {
            log.warn("Song with id '{}' not found!", id);
            throw new Mp3NotFoundException("Mp3 file not found!");
        }
    }

    @Override
    public List<Song> getAllSongsInfo() {
        List<Song> songs = songRepository.findAll();
        log.info("All '{}' songs was returned.", songs.size());
        return songs;
    }

    @Override
    public void deleteSong(Long id) {
        if (songRepository.existsById(id)) {
            Song song = songRepository.getById(id);
            resourceStorage.deleteFileByName(song.getName());
            songRepository.delete(song);
            log.info("Song with id '{}' has been deleted.", id);
        } else {
            log.warn("Song with id '{}' not found!", id);
            throw new Mp3NotFoundException("Mp3 file not found!");
        }

    }

    @Override
    public List<Long> deleteSongsFromAlbum(Long albumId) {
        if (getSongsFromAlbum(albumId) != null) {
            List<Song> songs = songRepository.findAllByAlbumId(albumId);
            List<Long> ids = songs.stream().map(Song::getId).collect(Collectors.toList());
            songRepository.deleteAllByAlbumId(albumId);
            log.info("All songs with ids '{}' which belongs to the album with id '{}' was removed", ids, albumId);
            return ids;
        } else {
            log.warn("Album with id '{}' not found in albums-microservice", albumId);
            throw new EntityNotExistsException(String.format("Album with id '%s' not found in albums-microservice", albumId));
        }
    }

    @Override
    public List<Song> getSongsFromAlbum(Long albumId) {
        Album album = albumsApiClient.getAlbumById(albumId);
        if (album != null) {
            List<Song> songs = songRepository.findAllByAlbumId(albumId);
            log.info("For album with id '{}' was found '{}' songs.", albumId, songs.size());
            return songs;
        } else {
            log.warn("Can't find album with id '{}' from albums-microservice!", albumId);
            throw new EntityNotExistsException(String.format("Can't find artist with id '%s' from artist-microservice!", albumId));
        }
    }

    @Override
    public Album getSongAlbum(Song song) throws EntityNotExistsException {
        Album album = albumsApiClient.getAlbumById(song.getAlbumId());
        if (album != null) {
            log.info("Album has been received from albums-microservice.");
            return album;
        }
        else {
            log.warn("Can't get album with id '{}' from albums-microservice!", song.getAlbumId());
            throw new EntityNotExistsException(String.format("Can't get artist with id '%s' from artist-microservice!", song.getAlbumId()));
        }
    }

}
