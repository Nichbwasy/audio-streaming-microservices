package com.epam.audio.streaming.album.microservice.services;

import com.epam.audio.streaming.album.microservice.dao.repositories.AlbumRepository;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.album.AlbumNotExistsException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.album.microservice.services.dto.Artist;
import com.epam.audio.streaming.album.microservice.services.implimentations.AlbumsServiceImpl;
import org.junit.jupiter.api.Assertions;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AlbumServiceTest {

    private final static Album testAlbum = new Album(1L, "Name", 2000, "Notes", 1L);
    private final static Artist testArtist = new Artist(1L,  "Arist Name", "Album notes", null);
    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private RestTemplate restTemplate;

    @InjectMocks
    private AlbumsServiceImpl albumsService;

    @Test
    public void addAlbumWithExistedArtistTest() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
                )).thenReturn(ResponseEntity.ok().body(testArtist));
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);

        Assertions.assertNotNull(albumsService.addAlbum(testAlbum));
    }

    @Test
    public void addAlbumWithNotExistedArtistTest() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.internalServerError().body(null));
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);

        Assertions.assertThrows(EntityNotExistsException.class, () -> albumsService.addAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithExistedAlbumAndArtist() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.ok().body(testArtist));
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);

        Assertions.assertNotNull(albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithNotExistedAlbumAndExistedArtist() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.ok().body(testArtist));
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);

        Assertions.assertThrows(AlbumNotExistsException.class, () -> albumsService.updateAlbum(testAlbum));
    }

    @Test
    public void updateAlbumTestWithExistedAlbumAndNotExistedArtist() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.internalServerError().body(null));
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.save(testAlbum)).thenReturn(testAlbum);

        Assertions.assertThrows(EntityNotExistsException.class, () -> albumsService.updateAlbum(testAlbum));
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
    public void getAllAlbums() {
        Mockito.when(albumRepository.findAll()).thenReturn(new ArrayList<>());

        Assertions.assertNotNull(albumsService.getAllAlbums());
    }

    @Test
    public void deleteExistedAlbums() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.internalServerError().body(null));
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.existsById(2L)).thenReturn(true);
        Mockito.when(albumRepository.existsById(3L)).thenReturn(true);
        Mockito.doNothing().when(albumRepository).deleteById(Mockito.anyLong());


        List<Long> listIds = albumsService.deleteAlbums(idsToDelete);
        Assertions.assertEquals(listIds.get(0), idsToDelete.get(0));
        Assertions.assertEquals(listIds.get(1), idsToDelete.get(1));
        Assertions.assertEquals(listIds.get(2), idsToDelete.get(2));
    }

    @Test
    public void deleteNotExistedAlbums() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.internalServerError().body(null));
        Mockito.when(albumRepository.existsById(1L)).thenReturn(true);
        Mockito.when(albumRepository.existsById(2L)).thenReturn(false);
        Mockito.when(albumRepository.existsById(3L)).thenReturn(true);
        Mockito.doNothing().when(albumRepository).deleteById(Mockito.anyLong());

        List<Long> listIds = albumsService.deleteAlbums(idsToDelete);
        Assertions.assertEquals(listIds.get(0), idsToDelete.get(0));
        Assertions.assertEquals(listIds.get(1), idsToDelete.get(2));
    }

    @Test
    public void deleteWithNullAlbums() {
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(Class.class)
        )).thenReturn(ResponseEntity.internalServerError().body(null));
        Mockito.when(albumRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(albumRepository).deleteById(Mockito.anyLong());

        Assertions.assertThrows(NullPointerException.class, () -> albumsService.deleteAlbums(null));
    }

}
