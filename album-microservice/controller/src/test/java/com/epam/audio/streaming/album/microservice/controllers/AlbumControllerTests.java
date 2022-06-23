package com.epam.audio.streaming.album.microservice.controllers;

import com.epam.audio.streaming.album.microservice.dao.repositories.AlbumRepository;
import com.epam.audio.streaming.album.microservice.exceptions.EntityAlreadyExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.album.microservice.services.implimentations.AlbumsServiceImpl;
import com.epam.audio.streaming.artists.microservice.clients.ArtistsApiClient;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import com.epam.audio.streaming.songs.microservice.client.SongsApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {AlbumController.class})
@AutoConfigureMockMvc( addFilters = false ) // disable security
public class AlbumControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AlbumsServiceImpl albumsService;

    @MockBean
    private AlbumRepository albumRepository;

    @MockBean
    private ArtistsApiClient artistsApiClient;

    @MockBean
    private SongsApiClient songsApiClient;

    @Autowired
    private AlbumController albumController;

    private Album album = new Album(1L, "Album", 2001, "Notes", 1L);

    @Test
    public void albumsGetTest() throws Exception {
        Mockito.when(albumsService.getAllAlbums()).thenReturn(new ArrayList<Album>());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/albums"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void albumsGetWithErrorInServiceTest() throws Exception {
        Mockito.when(albumsService.getAllAlbums()).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/albums"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void  albumsGetByIdTest() throws Exception {
        Album album = new Album(1L, "Album", 2001, "Notes", 1L);
        Artist artist = new Artist(1L, "Artist", "Notes", null);

        Mockito.when(albumsService.getAlbum(1L)).thenReturn(album);
        Mockito.when(albumsService.getAlbumArtist(album)).thenReturn(artist);

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/albums/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.album.id").value(album.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.album.name").value(album.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.album.year").value(album.getYear()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.album.notes").value(album.getNotes()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.album.artistId").value(album.getArtistId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist.id").value(artist.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist.name").value(artist.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist.notes").value(artist.getNotes()));
    }

    @Test
    public void  albumsGetByNotExistedIdTest() throws Exception {
        Mockito.when(albumsService.getAlbum(Mockito.anyLong())).thenThrow(EntityNotExistsException.class);
        Mockito.when(albumsService.getAlbumArtist(Mockito.any(Album.class))).thenReturn(new Artist());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/albums/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void  albumsGetByIdWithAnyServiceExceptionTest() throws Exception {
        Mockito.when(albumsService.getAlbum(Mockito.anyLong())).thenThrow(RuntimeException.class);
        Mockito.when(albumsService.getAlbumArtist(Mockito.any(Album.class))).thenReturn(new Artist());

        this.mockMvc
                .perform(MockMvcRequestBuilders.get("/albums/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void  saveAlbumTest() throws Exception {
        Mockito.when(albumsService.addAlbum(Mockito.any(Album.class))).thenReturn(album);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/albums")
                        .flashAttr("album", album)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1L));
    }

    @Test
    public void  saveAlreadyExistedAlbumTest() throws Exception {
        Mockito.when(albumsService.addAlbum(Mockito.any(Album.class))).thenThrow(EntityAlreadyExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/albums")
                        .flashAttr("album", album)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void  saveAlbumWithServiceErrorTest() throws Exception {
        Mockito.when(albumsService.addAlbum(Mockito.any(Album.class))).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/albums")
                        .flashAttr("album", album)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void  updateAlbumTest() throws Exception {
        Mockito.when(albumsService.updateAlbum(Mockito.any(Album.class))).thenReturn(album);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/albums/1")
                        .flashAttr("album", album)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void  updateNotExistedAlbumTest() throws Exception {
        Mockito.when(albumsService.updateAlbum(Mockito.any(Album.class))).thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/albums/1")
                        .flashAttr("album", album)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void  updateAlbumWithServiceExceptionTest() throws Exception {
        Mockito.when(albumsService.updateAlbum(Mockito.any(Album.class))).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/albums/1")
                        .flashAttr("album", album)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void deleteAlbumTest() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        ids.add(3L);

        Mockito.when(albumsService.deleteAlbums(Mockito.anyList())).thenReturn(ids);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/albums")
                        .param("id", "1,2,3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteWithServiceException() throws Exception {
        Mockito.when(albumsService.deleteAlbums(Mockito.anyList())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/albums")
                        .param("id", "1, 2, 3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getFilteredAlbumsTest() throws Exception{
        Mockito.when(albumsService.getFilteredAlbums("Album", 2001))
                .thenReturn(Arrays.asList(album));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/albums/filter")
                        .param("name", "Album")
                        .param("year", "2001")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1L));
    }

    @Test
    public void getFilteredAlbumsWithServiceExceptionTest() throws Exception{
        Mockito.when(albumsService.getFilteredAlbums(Mockito.anyString(), Mockito.anyInt()))
                .thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/albums/filter")
                        .param("name", "Album")
                        .param("year", "2001")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}
