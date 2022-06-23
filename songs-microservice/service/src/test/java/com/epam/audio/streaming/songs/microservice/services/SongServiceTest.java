package com.epam.audio.streaming.songs.microservice.services;

import com.epam.audio.streaming.album.microservice.clients.AlbumsApiClient;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.songs.microservice.dao.repositories.SongRepository;
import com.epam.audio.streaming.songs.microservice.dao.repositories.StorageRepository;
import com.epam.audio.streaming.songs.microservice.dao.storages.ResourceStorage;
import com.epam.audio.streaming.songs.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3NotFoundException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3ValidationException;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.models.Storage;
import com.epam.audio.streaming.songs.microservice.services.config.SongsServiceTestConfiguration;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.InputStream;
import java.util.*;

@SpringBootTest(classes = SongsService.class)
@Import(SongsServiceTestConfiguration.class)
@ExtendWith(SpringExtension.class)
public class SongServiceTest {

    private final static Album testAlbum = new Album(1L, "Name", 2000, "Notes", 1L);
    private final static Song testSong = new Song(1L,  "Name", 2000, "Notes", null, 1L);
    private final static Storage testStorage = new Storage(1L,  "Storage type");

    @Autowired
    private SongsService songService;

    @MockBean
    private SongRepository songRepository;

    @MockBean
    private StorageRepository storageRepository;

    @MockBean
    private ResourceStorage resourceStorage;

    @MockBean
    private AlbumsApiClient albumsApiClient;

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
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);

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
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);

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
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);

        Assertions.assertThrows(NullPointerException.class,
                () -> songService.saveSong(null, null));
    }

    @SneakyThrows
    @Test
    public void saveSongRepositoryExceptionTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("TestTxtFile.txt");
        Map<String, String> resourceStorageMap = new HashMap<>();
        resourceStorageMap.put("name", "value");

        Mockito.when(storageRepository.getByType(Mockito.anyString())).thenThrow(RuntimeException.class);
        Mockito.when(resourceStorage.saveFile(Mockito.anyString(), Mockito.any(InputStream.class))).thenReturn(resourceStorageMap);
        Mockito.when(songRepository.save(Mockito.any(Song.class))).thenReturn(testSong);
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);

        Assertions.assertThrows(RuntimeException.class,
                () -> songService.saveSong(testSong, IOUtils.toByteArray(fileInputStream)));
    }

    @SneakyThrows
    @Test
    public void saveSongAlbumNotFoundTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("SomeMp3File.mp3");
        Map<String, String> resourceStorageMap = new HashMap<>();
        resourceStorageMap.put("name", "value");

        Mockito.when(storageRepository.getByType(Mockito.anyString())).thenReturn(testStorage);
        Mockito.when(resourceStorage.saveFile(Mockito.anyString(), Mockito.any(InputStream.class))).thenReturn(resourceStorageMap);
        Mockito.when(songRepository.save(Mockito.any(Song.class))).thenReturn(testSong);
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(null);

        Assertions.assertThrows(EntityNotExistsException.class,
                () -> songService.saveSong(testSong, IOUtils.toByteArray(fileInputStream)));
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
    public void getSongWithRepositoryExceptionTest() {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("SomeMp3File.mp3");
        Mockito.when(songRepository.existsById(1L)).thenReturn(true);
        Mockito.when(songRepository.getById(1L)).thenThrow(RuntimeException.class);
        Mockito.when(resourceStorage.getFileByName("Name")).thenReturn(fileInputStream);

        Assertions.assertThrows(RuntimeException.class, () -> songService.getSong(1L));
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
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);

        List<Long> deletedIds = songService.deleteSongsFromAlbum(1L);
        Assertions.assertEquals(deletedIds.get(0), 1L);
        Assertions.assertEquals(deletedIds.get(1), 2L);
        Assertions.assertEquals(deletedIds.get(2), 3L);
    }

    @Test
    public void deleteSongsFromNotExistedAlbumTest() {
        Mockito.when(songRepository.findAllByAlbumId(Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(songRepository).deleteAllByAlbumId(Mockito.anyLong());
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(null);

        Assertions.assertThrows(EntityNotExistsException.class, () -> songService.getSongsFromAlbum(1L));
    }

    @Test
    public void deleteSongsWithRepositoryExceptionTest() {
        Mockito.when(songRepository.findAllByAlbumId(Mockito.anyLong())).thenThrow(RuntimeException.class);
        Mockito.doNothing().when(songRepository).deleteAllByAlbumId(Mockito.anyLong());
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(null);

        Assertions.assertThrows(RuntimeException.class, () -> songService.getSongsFromAlbum(1L));
    }

    @Test
    public void deleteSongsWithAlbumsApiExceptionTest() {
        Mockito.when(songRepository.findAllByAlbumId(Mockito.anyLong())).thenReturn(null);
        Mockito.doNothing().when(songRepository).deleteAllByAlbumId(Mockito.anyLong());
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> songService.getSongsFromAlbum(1L));
    }

    @Test
    public void getSongAlbumTest() {
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);
        Album album = songService.getSongAlbum(testSong);
        Assertions.assertEquals(album.getId(), testAlbum.getId());
        Assertions.assertEquals(album.getName(), testAlbum.getName());
        Assertions.assertEquals(album.getNotes(), testAlbum.getNotes());
        Assertions.assertEquals(album.getYear(), testAlbum.getYear());
        Assertions.assertEquals(album.getArtistId(), testAlbum.getArtistId());
    }

    @Test
    public void getNotExistsSongAlbumTest() {
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(null);

        Assertions.assertThrows(EntityNotExistsException.class, () -> songService.getSongAlbum(testSong));
    }

    @Test
    public void getNullableSongAlbumTest() {
        Mockito.when(albumsApiClient.getAlbumById(1L)).thenReturn(testAlbum);

        Assertions.assertThrows(NullPointerException.class, () -> songService.getSongAlbum(null));
    }

    @Test
    public void getSongAlbumWithApiExceptionTest() {
        Mockito.when(albumsApiClient.getAlbumById(Mockito.anyLong())).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> songService.getSongAlbum(testSong));
    }
}
