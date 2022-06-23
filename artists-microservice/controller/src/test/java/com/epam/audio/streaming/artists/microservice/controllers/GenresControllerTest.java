package com.epam.audio.streaming.artists.microservice.controllers;

import com.epam.audio.streaming.artists.microservice.dao.repositories.ArtistsRepository;
import com.epam.audio.streaming.artists.microservice.dao.repositories.GenresRepository;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityAlreadyExistsException;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.GenresService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {GenresController.class})
@AutoConfigureMockMvc( addFilters = false ) // disable security
public class GenresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistsRepository artistRepository;

    @MockBean
    private GenresRepository genreRepository;

    @MockBean
    private GenresService genresService;

    @Autowired
    private GenresController genresController;

    private Genre genre = new Genre(1L, "Genre");

    @Test
    public void getGenresTest() throws Exception {
        Mockito.when(genresService.getAllGenres()).thenReturn(Arrays.asList(genre));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/genres")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getGenresWithServiceExceptionTest() throws Exception {
        Mockito.when(genresService.getAllGenres()).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/genres")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveGenreTest() throws Exception{
        Mockito.when(genresService.addGenre("Genre")).thenReturn(genre);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/genres")
                        .flashAttr("genre", genre)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1L));
    }

    @Test
    public void saveAlreadyExistedGenreTest() throws Exception{
        Mockito.when(genresService.addGenre("Genre")).thenThrow(EntityAlreadyExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/genres")
                        .flashAttr("genre", genre)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveGenreWithServiceExceptionTest() throws Exception{
        Mockito.when(genresService.addGenre("Genre")).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/genres")
                        .flashAttr("genre", genre)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteGenresTest() throws Exception{
        Mockito.when(genresService.deleteGenres(Arrays.asList(1L, 2L, 3L))).thenReturn(Arrays.asList(1L, 2L, 3L));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/genres")
                        .param("id", "1,2,3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteNotExistedGenresTest() throws Exception{
        Mockito.when(genresService.deleteGenres(Mockito.anyList())).thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/genres")
                        .param("id", "1,2,3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void deleteGenresWithServiceExceptionTest() throws Exception{
        Mockito.when(genresService.deleteGenres(Mockito.anyList())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/genres")
                        .param("id", "1,2,3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
