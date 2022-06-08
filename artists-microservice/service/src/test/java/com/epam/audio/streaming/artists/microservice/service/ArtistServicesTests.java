package com.epam.audio.streaming.artists.microservice.service;

import com.epam.audio.streaming.artists.microservice.dao.repositories.ArtistsRepository;
import com.epam.audio.streaming.artists.microservice.dao.repositories.GenresRepository;
import com.epam.audio.streaming.artists.microservice.exceptions.artists.ArtistGenresValidationException;
import com.epam.audio.streaming.artists.microservice.exceptions.artists.ArtistNotExistException;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.implimentations.ArtistsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ArtistServicesTests {

    private final static Genre testGenre = new Genre(1L, "Pop");
    private final static Artist testArtist = new Artist(1L, "Artist1", "Notes1", new ArrayList<>(Arrays.asList(testGenre)));

    @MockBean
    private ArtistsRepository artistsRepository;

    @MockBean
    private GenresRepository genreRepository;

    @InjectMocks
    private ArtistsServiceImpl artistsService;

    @Test
    public void addArtistTest() {
        Mockito.when(genreRepository.existsById(1L)).thenReturn(true);
        Mockito.when(artistsRepository.save(Mockito.any(Artist.class))).thenReturn(testArtist);

        Artist result = artistsService.addArtist("Artist1", "Notes1", new ArrayList<>(Arrays.asList(1L)));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), testArtist.getId());
        Assertions.assertEquals(result.getName(), testArtist.getName());
        Assertions.assertEquals(result.getNotes(), testArtist.getNotes());
    }

    @Test
    public void addArtistWithNotExistedGenreTest() {
        Mockito.when(genreRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(artistsRepository.save(Mockito.any(Artist.class))).thenReturn(testArtist);

        Assertions.assertThrows(ArtistGenresValidationException.class,
                () -> artistsService.addArtist("Artist1", "Notes1", new ArrayList<>(Arrays.asList(1L))));
    }

    @Test
    public void updateExistedArtistTest() {
        Mockito.when(genreRepository.existsById(1L)).thenReturn(true);
        Mockito.when(artistsRepository.existsById(1L)).thenReturn(true);
        Mockito.when(genreRepository.getById(1L)).thenReturn(testGenre);
        Mockito.when(artistsRepository.save(Mockito.any(Artist.class))).thenReturn(testArtist);

        Artist result = artistsService.updateArtist(1L,"Artist1", "Notes1", new ArrayList<>(Arrays.asList(1L)));
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), testArtist.getId());
        Assertions.assertEquals(result.getName(), testArtist.getName());
        Assertions.assertEquals(result.getNotes(), testArtist.getNotes());
    }

    @Test
    public void updateNotExistedArtistTest() {
        Mockito.when(genreRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(artistsRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(genreRepository.getById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(artistsRepository.save(Mockito.any(Artist.class))).thenReturn(testArtist);

        Assertions.assertThrows(ArtistNotExistException.class,
                () -> artistsService.updateArtist(1L,"Artist1", "Notes1", new ArrayList<>(Arrays.asList(1L))));

    }

    @Test
    public void getExistedArtistByIdTest() {
        Mockito.when(artistsRepository.existsById(1L)).thenReturn(true);
        Mockito.when(artistsRepository.findById(1L)).thenReturn(Optional.of(testArtist));

        Assertions.assertNotNull(artistsService.getArtist(1L));
    }

    @Test
    public void getNotExistedArtistByIdTest() {
        Mockito.when(artistsRepository.existsById(Mockito.anyLong())).thenReturn(false);
        Mockito.when(artistsRepository.findById(Mockito.anyLong())).thenReturn(null);

        Assertions.assertThrows(ArtistNotExistException.class, () -> artistsService.getArtist(1L));
    }

    @Test
    public void deleteExistedArtist() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Mockito.when(artistsRepository.existsById(1L)).thenReturn(true);
        Mockito.when(artistsRepository.existsById(2L)).thenReturn(true);
        Mockito.when(artistsRepository.existsById(3L)).thenReturn(true);
        Mockito.doNothing().when(artistsRepository).deleteById(1L);
        Mockito.doNothing().when(artistsRepository).deleteById(2L);
        Mockito.doNothing().when(artistsRepository).deleteById(3L);

        List<Long> deleted = artistsService.deleteArtists(idsToDelete);
        Assertions.assertEquals(deleted.get(0), idsToDelete.get(0));
        Assertions.assertEquals(deleted.get(1), idsToDelete.get(1));
        Assertions.assertEquals(deleted.get(2), idsToDelete.get(2));
    }

    @Test
    public void deleteWithNotExistedArtist() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Mockito.when(artistsRepository.existsById(1L)).thenReturn(true);
        Mockito.when(artistsRepository.existsById(2L)).thenReturn(false);
        Mockito.when(artistsRepository.existsById(3L)).thenReturn(true);
        Mockito.doNothing().when(artistsRepository).deleteById(1L);
        Mockito.doNothing().when(artistsRepository).deleteById(3L);

        List<Long> deleted = artistsService.deleteArtists(idsToDelete);
        Assertions.assertEquals(deleted.get(0), idsToDelete.get(0));
        Assertions.assertEquals(deleted.get(1), idsToDelete.get(2));
    }

    @Test
    public void deleteWithNullArtist() {
        Mockito.when(artistsRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(artistsRepository).deleteById(Mockito.anyLong());

        Assertions.assertThrows(NullPointerException.class,() -> artistsService.deleteArtists(null));
    }


}
