package com.epam.audio.streaming.songs.microservice.services;

import com.epam.audio.streaming.songs.microservice.dao.repositories.SongRepository;
import com.epam.audio.streaming.songs.microservice.dao.repositories.StorageRepository;
import com.epam.audio.streaming.songs.microservice.dao.storages.ResourceStorage;
import com.epam.audio.streaming.songs.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3NotFoundException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3ValidationException;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.models.Storage;
import com.epam.audio.streaming.songs.microservice.services.dto.Album;
import com.epam.audio.streaming.songs.microservice.services.implimentations.SongServiceImpl;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SongServiceTest {

    private final static Album testAlbum = new Album(1L, "Name", 2000, "Notes", 1L);
    private final static Song testSong = new Song(1L,  "Name", 2000, "Notes", null, 1L);
    private final static Storage testStorage = new Storage(1L,  "Storage type");

    @MockBean
    private SongRepository songRepository;

    @MockBean
    private StorageRepository storageRepository;

    @MockBean
    private ResourceStorage resourceStorage;

    @MockBean
    private static RestTemplate restTemplate;

    @InjectMocks
    private SongServiceImpl songService;

    @BeforeEach
    public void restTemplateBeanConf() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.ok().body(testAlbum));
    }

    @SneakyThrows
    @Test
    public void saveSongTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("SomeMp3File.mp3");
        Map<String, String> resourceStorageMap = new HashMap<>();
        resourceStorageMap.put("name", "value");

        Mockito.when(storageRepository.getByType(Mockito.anyString())).thenReturn(testStorage);
        Mockito.when(resourceStorage.saveFile(Mockito.anyString(), Mockito.any(InputStream.class))).thenReturn(resourceStorageMap);
        Mockito.when(songRepository.save(Mockito.any(Song.class))).thenReturn(testSong);

        Assertions.assertNotNull(songService.saveSong(testSong, IOUtils.toByteArray(fileInputStream)));
    }

    @SneakyThrows
    @Test
    public void saveSongWrongFileExtensionTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("TestTxtFile.txt");
        Map<String, String> resourceStorageMap = new HashMap<>();
        resourceStorageMap.put("name", "value");

        Mockito.when(storageRepository.getByType(Mockito.anyString())).thenReturn(testStorage);
        Mockito.when(resourceStorage.saveFile(Mockito.anyString(), Mockito.any(InputStream.class))).thenReturn(resourceStorageMap);
        Mockito.when(songRepository.save(Mockito.any(Song.class))).thenReturn(testSong);

        Assertions.assertThrows(Mp3ValidationException.class,
                () -> songService.saveSong(testSong, IOUtils.toByteArray(fileInputStream)));
    }

    @SneakyThrows
    @Test
    public void saveSongNullValuesTest() {
        Map<String, String> resourceStorageMap = new HashMap<>();
        resourceStorageMap.put("name", "value");

        Mockito.when(storageRepository.getByType(Mockito.anyString())).thenReturn(testStorage);
        Mockito.when(resourceStorage.saveFile(Mockito.anyString(), Mockito.any(InputStream.class))).thenReturn(resourceStorageMap);
        Mockito.when(songRepository.save(Mockito.any(Song.class))).thenReturn(testSong);

        Assertions.assertThrows(NullPointerException.class,
                () -> songService.saveSong(null, null));
    }

    @Test
    public void getSongTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("SomeMp3File.mp3");
        Mockito.when(songRepository.existsById(1L)).thenReturn(true);
        Mockito.when(songRepository.getById(1L)).thenReturn(testSong);
        Mockito.when(resourceStorage.getFileByName("Name")).thenReturn(fileInputStream);

        Assertions.assertNotNull(songService.getSong(1L));
    }

    @Test
    public void getSongByNotExistedIdTest() {
        Mockito.when(songRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(songRepository.getById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(resourceStorage.getFileByName(Mockito.anyString())).thenReturn(null);

        Assertions.assertThrows(Mp3NotFoundException.class, () -> songService.getSong(null));
    }

    @Test
    public void getSongByNullIdTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("TestTxtFile.txt");
        Mockito.when(songRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(songRepository.getById(Mockito.anyLong())).thenReturn(testSong);
        Mockito.when(resourceStorage.getFileByName(Mockito.anyString())).thenReturn(fileInputStream);

        Assertions.assertThrows(Mp3NotFoundException.class, () -> songService.getSong(1L));
    }

    @Test
    public void deleteSongTest() {
        Mockito.when(songRepository.existsById(1L)).thenReturn(true);
        Mockito.when(songRepository.getById(1L)).thenReturn(testSong);
        Mockito.doNothing().when(resourceStorage).deleteFileByName(Mockito.anyString());
        Mockito.doNothing().when(songRepository).delete(Mockito.any(Song.class));

        Assertions.assertDoesNotThrow(() -> songService.deleteSong(1L));
    }

    @Test
    public void deleteNotExistedSongTest() {
        Mockito.when(songRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(songRepository.getById(Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(resourceStorage).deleteFileByName(Mockito.anyString());
        Mockito.doNothing().when(songRepository).delete(Mockito.any(Song.class));

        Assertions.assertThrows(Mp3NotFoundException.class, () -> songService.deleteSong(1L));
    }

    @Test
    public void deleteSongWithNullableIdTest() {
        Mockito.when(songRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.when(songRepository.getById(Mockito.anyLong())).thenReturn(testSong);
        Mockito.doNothing().when(resourceStorage).deleteFileByName(Mockito.anyString());
        Mockito.doNothing().when(songRepository).delete(Mockito.any(Song.class));

        Assertions.assertThrows(Mp3NotFoundException.class, () -> songService.deleteSong(null));
    }

    @Test
    public void deleteSongsFromAlbumTest() {
        List<Song> songs = new ArrayList<>(Arrays.asList(
                new Song(1L,  "Name1", 2001, "Notes1", null, 1L),
                new Song(2L,  "Name2", 2002, "Notes2", null, 1L),
                new Song(3L,  "Name3", 2003, "Notes3", null, 1L)
        ));
        Mockito.when(songRepository.findAllByAlbumId(1L)).thenReturn(songs);
        Mockito.doNothing().when(songRepository).deleteAllByAlbumId(Mockito.anyLong());

        List<Long> deletedIds = songService.deleteSongsFromAlbum(1L);
        Assertions.assertEquals(deletedIds.get(0), 1L);
        Assertions.assertEquals(deletedIds.get(1), 2L);
        Assertions.assertEquals(deletedIds.get(2), 3L);
    }

    @Test
    public void deleteSongsFromNotExistedAlbumTest() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.ok().body(null));
        Mockito.when(songRepository.findAllByAlbumId(Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(songRepository).deleteAllByAlbumId(Mockito.anyLong());

        Assertions.assertThrows(EntityNotExistsException.class, () -> songService.getSongsFromAlbum(1L));
    }

    @Test
    public void getSongAlbumTest() {
        Album album = songService.getSongAlbum(testSong);
        Assertions.assertEquals(album.getId(), testAlbum.getId());
        Assertions.assertEquals(album.getName(), testAlbum.getName());
        Assertions.assertEquals(album.getNotes(), testAlbum.getNotes());
        Assertions.assertEquals(album.getYear(), testAlbum.getYear());
        Assertions.assertEquals(album.getArtistId(), testAlbum.getArtistId());
    }

    @Test
    public void getNotExistsSongAlbumTest() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.ok().body(null));

        Assertions.assertThrows(EntityNotExistsException.class, () -> songService.getSongAlbum(testSong));
    }

    @Test
    public void getNullableSongAlbumTest() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.ok().body(null));

        Assertions.assertThrows(NullPointerException.class, () -> songService.getSongAlbum(null));
    }
}
