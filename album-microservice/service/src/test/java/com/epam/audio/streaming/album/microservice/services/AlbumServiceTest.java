package com.epam.audio.streaming.album.microservice.services;

import com.epam.audio.streaming.album.microservice.dao.repositories.AlbumRepository;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.album.AlbumNotExistsException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.album.microservice.services.config.AlbumsServiceTestsConfiguration;
import com.epam.audio.streaming.artists.microservice.clients.ArtistsApiClient;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.songs.microservice.client.SongsApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = AlbumsService.class)
@Import(AlbumsServiceTestsConfiguration.class)
@ExtendWith(SpringExtension.class)
public class AlbumServiceTest {

    private final static Album testAlbum = new Album(1L, "Name", 2000, "Notes", 1L);
    private final static Artist testArtist = new Artist(1L,  "Arist Name", "Album notes", null);

    @Autowired
    private AlbumsService albumsService;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private ArtistsApiClient artistsApiClient;

    @MockBean
    private SongsApiClient songsApiClient;

    @Test
    public void addAlbumWithExistedArtistTest() {
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenReturn(testArtist);

        Assertions.assertNotNull(albumsService.addAlbum(testAlbum));
    }

    @Test
    public void addAlbumWithNotExistedArtistTest() {
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenThrow(EntityNotExistsException.class);

        Assertions.assertThrows(EntityNotExistsException.class, () -> albumsService.addAlbum(testAlbum));
    }

    @Test
    public void addAlbumWithApiArtistExceptionTest() {
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.addAlbum(testAlbum));
    }

    @Test
    public void addAlbumWithServiceExceptionTest() {
        Mockito.when(albumRepository.save(testAlbum)).thenThrow(RuntimeException.class);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenReturn(testArtist);

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.addAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithExistedAlbumAndArtist() {
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenReturn(testArtist);

        Assertions.assertNotNull(albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithNotExistedAlbumAndExistedArtist() {
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenReturn(testArtist);

        Assertions.assertThrows(EntityNotExistsException.class, () -> albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithExistedAlbumAndNotExistedArtist() {
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenThrow(EntityNotExistsException.class);

        Assertions.assertThrows(EntityNotExistsException.class, () -> albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithServiceException() {
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.save(testAlbum)).thenThrow(RuntimeException.class);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenReturn(testArtist);

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithArtistApiException() {
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(artistsApiClient.getArtistById(1L)).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void getExistedAlbumTest() {
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.findById(1L)).thenReturn(Optional.of(testAlbum));

        Assertions.assertNotNull(albumsService.getAlbum(1L));
    }

    @Test
    public void getNotExistedAlbumTest() {
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(albumRepository.findById(Mockito.anyLong())).thenReturn(null);

        Assertions.assertThrows(AlbumNotExistsException.class, () -> albumsService.getAlbum(1L));
    }

    @Test
    public void getAlbumWithServiceExceptionTest() {
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(albumRepository.findById(Mockito.anyLong())).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.getAlbum(1L));
    }

    @Test
    public void getAllAlbumsTest() {
        Mockito.when(albumRepository.findAll()).thenReturn(Arrays.asList(testAlbum));

        Assertions.assertNotNull(albumsService.getAllAlbums());
    }

    @Test
    public void getAllAlbumsWithServiceExceptionTest() {
        Mockito.when(albumRepository.findAll()).thenThrow(RuntimeException.class);

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.getAllAlbums());
    }

    @Test
    public void deleteExistedAlbumsTest() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.existsById(2L)).thenReturn(true);
        Mockito.when(albumRepository.existsById(3L)).thenReturn(true);
        Mockito.when(songsApiClient.deleteSongByAlbumId(Mockito.anyLong())).thenReturn(Arrays.asList(1L, 2L));
        Mockito.doNothing().when(albumRepository).deleteById(Mockito.anyLong());


        List<Long> listIds = albumsService.deleteAlbums(idsToDelete);
        Assertions.assertEquals(listIds.get(0), idsToDelete.get(0));
        Assertions.assertEquals(listIds.get(1), idsToDelete.get(1));
        Assertions.assertEquals(listIds.get(2), idsToDelete.get(2));
    }

    @Test
    public void deleteNotExistedAlbumsTest() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.existsById(2L)).thenReturn(false);
        Mockito.when(albumRepository.existsById(3L)).thenReturn(true);
        Mockito.when(songsApiClient.deleteSongByAlbumId(Mockito.anyLong())).thenReturn(Arrays.asList(1L, 2L));
        Mockito.doNothing().when(albumRepository).deleteById(Mockito.anyLong());

        List<Long> listIds = albumsService.deleteAlbums(idsToDelete);
        Assertions.assertEquals(listIds.get(0), idsToDelete.get(0));
        Assertions.assertEquals(listIds.get(1), idsToDelete.get(2));
    }

    @Test
    public void deleteWithNullAlbumsTest() {
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(albumRepository).deleteById(Mockito.anyLong());

        Assertions.assertThrows(NullPointerException.class, () -> albumsService.deleteAlbums(null));
    }

    @Test
    public void deleteAlbumsWithServiceExceptionTest() {
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doThrow(RuntimeException.class).when(albumRepository).deleteById(Mockito.anyLong());

        Assertions.assertThrows(RuntimeException.class, () -> albumsService.deleteAlbums(null));
    }

}
