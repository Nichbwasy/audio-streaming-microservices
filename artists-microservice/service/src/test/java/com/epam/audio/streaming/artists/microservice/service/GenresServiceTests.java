package com.epam.audio.streaming.artists.microservice.service;

import com.epam.audio.streaming.artists.microservice.dao.repositories.GenresRepository;
import com.epam.audio.streaming.artists.microservice.exceptions.genres.GenreAlreadyExistsException;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.implimentations.GenresServiceImpl;
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
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class GenresServiceTests {

    @MockBean
    private GenresRepository genresRepository;

    @InjectMocks
    private GenresServiceImpl genresService;

    private final static Genre testGenre = new Genre(1L, "Genre");

    @Test
    public void addNewGenreTest() {
        Mockito.when(genresRepository.existsByName(Mockito.anyString())).thenReturn(false);
        Mockito.when(genresRepository.save(Mockito.any(Genre.class))).thenReturn(testGenre);

        Genre rez = genresService.addGenre("Genre");
        Assertions.assertNotNull(rez);
    }

    @Test
    public void addExistedGenreTest() {
        Mockito.when(genresRepository.existsByName("Genre")).thenReturn(true);
        Mockito.when(genresRepository.save(Mockito.any(Genre.class))).thenReturn(testGenre);

        Assertions.assertThrows(GenreAlreadyExistsException.class, () -> genresService.addGenre("Genre"));
    }

    @Test
    public void deleteGenresTest() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        Mockito.when(genresRepository.existsById(1L)).thenReturn(true);
        Mockito.when(genresRepository.existsById(2L)).thenReturn(true);
        Mockito.when(genresRepository.existsById(3L)).thenReturn(true);
        Mockito.doNothing().when(genresRepository).deleteById(Mockito.anyLong());


        List<Long> deleted = genresService.deleteGenres(idsToDelete);
        AtomicReference<Integer> i = new AtomicReference<>(0);
        deleted.forEach(id -> {
            Assertions.assertEquals(id, idsToDelete.get(i.get()));
            i.getAndSet(i.get() + 1);
        });
    }

    @Test
    public void deleteNotExistedGenresTest() {
        List<Long> idsToDelete = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        Mockito.when(genresRepository.existsById(1L)).thenReturn(true);
        Mockito.when(genresRepository.existsById(2L)).thenReturn(false);
        Mockito.when(genresRepository.existsById(3L)).thenReturn(true);
        Mockito.doNothing().when(genresRepository).deleteById(Mockito.anyLong());


        List<Long> deleted = genresService.deleteGenres(idsToDelete);
        Assertions.assertEquals(deleted.get(0), 1L);
        Assertions.assertEquals(deleted.get(1), 3L);
    }

    @Test
    public void deleteNullableGenresTest() {
        Mockito.when(genresRepository.existsById(Mockito.anyLong())).thenReturn(true);
        Mockito.doNothing().when(genresRepository).deleteById(Mockito.anyLong());

        Assertions.assertThrows(NullPointerException.class, () -> genresService.deleteGenres(null));
    }
}
