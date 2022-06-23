package com.epam.audio.streaming.artists.microservice.controllers;


import com.epam.audio.streaming.artists.microservice.controllers.dto.ArtistFormDTO;
import com.epam.audio.streaming.artists.microservice.dao.repositories.ArtistsRepository;
import com.epam.audio.streaming.artists.microservice.dao.repositories.GenresRepository;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.artists.microservice.models.Genre;
import com.epam.audio.streaming.artists.microservice.service.ArtistsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {ArtistsController.class})
@AutoConfigureMockMvc( addFilters = false ) // disable security
public class ArtistsControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtistsRepository artistRepository;

    @MockBean
    private GenresRepository genreRepository;

    @MockBean
    private ArtistsService artistsService;

    @Autowired
    private ArtistsController artistsController;

    private Artist artist = new Artist(1L, "Name", "Notes", Arrays.asList(new Genre(1L, "Genre1")));
    private Genre genre = new Genre(1L, "Genre1");

    @Test
    public void getArtistTest() throws Exception{
        Mockito.when(artistsService.getArtist(1L)).thenReturn(artist);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/artists/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(artist.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(artist.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.notes").value(artist.getNotes()));
    }

    @Test
    public void getNotExistedArtistTest() throws Exception{
        Mockito.when(artistsService.getArtist(Mockito.anyLong())).thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/artists/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getArtistWithServiceExceptionTest() throws Exception{
        Mockito.when(artistsService.getArtist(Mockito.anyLong())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/artists/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveArtistTest() throws Exception{
        ArtistFormDTO formData = new ArtistFormDTO();
        formData.setName("Artist");
        formData.setNotes("Notes");
        formData.setGenresIds(null);

        Mockito.when(artistsService.addArtist(
                formData.getName(),
                formData.getNotes(),
                formData.getGenresIds()))
                .thenReturn(artist);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/artists")
                        .flashAttr("artistFormDTO", formData)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveArtistWithNotExistedGenresTest() throws Exception{
        ArtistFormDTO formData = new ArtistFormDTO();
        formData.setName("Artist");
        formData.setNotes("Notes");
        formData.setGenresIds(Arrays.asList(11L, 22L));

        Mockito.when(artistsService.addArtist(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyList()))
                .thenThrow(EntityValidationException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/artists")
                        .flashAttr("artistFormDTO", formData)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveArtistWithServiceExceptionTest() throws Exception{
        ArtistFormDTO formData = new ArtistFormDTO();
        formData.setName("Artist");
        formData.setNotes("Notes");
        formData.setGenresIds(null);

        Mockito.when(artistsService.addArtist(
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyList()))
                .thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/artists")
                        .flashAttr("artistFormDTO", formData)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updateArtistTest() throws Exception {
        ArtistFormDTO formData = new ArtistFormDTO();
        formData.setName("Artist");
        formData.setNotes("Notes");
        formData.setGenresIds(null);

        Mockito.when(artistsService.updateArtist(
                        Mockito.eq(1L),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyList()))
                .thenReturn(artist);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/artists/1")
                        .flashAttr("artistFormDTO", formData)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void updateNotExistedArtistTest() throws Exception {
        ArtistFormDTO formData = new ArtistFormDTO();

        Mockito.when(artistsService.updateArtist(1L, null, null, null))
                .thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/artists/1")
                        .flashAttr("artistFormDTO", formData)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void updateWithWrongGenresArtistTest() throws Exception {
        ArtistFormDTO formData = new ArtistFormDTO();

        Mockito.when(artistsService.updateArtist(1L, null, null, null))
                .thenThrow(EntityValidationException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/artists/1")
                        .flashAttr("artistFormDTO", formData)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void deleteArtists() throws Exception {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Mockito.when(artistsService.deleteArtists(Mockito.anyList())).thenReturn(ids);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/artists")
                        .param("id", "1,2,3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteArtistsWithServiceException() throws Exception {
        Mockito.when(artistsService.deleteArtists(Mockito.anyList())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/artists")
                        .param("id", "1,2,3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getFilteredArtists() throws Exception {
        Mockito.when(artistsService.getFilteredArtists("Artist", null)).thenReturn(Arrays.asList(artist));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/artists/filter")
                        .param("name", "Artist")
                        .param("genreNames", "")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getFilteredArtistsWithServiceException() throws Exception {
        Mockito.when(artistsService.getFilteredArtists(Mockito.anyString(), Mockito.anyList())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/artists/filter")
                        .param("name", "Artist")
                        .param("genreNames", "")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
