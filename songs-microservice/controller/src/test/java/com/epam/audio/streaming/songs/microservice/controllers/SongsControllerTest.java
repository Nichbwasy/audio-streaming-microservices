package com.epam.audio.streaming.songs.microservice.controllers;

import com.epam.audio.streaming.album.microservice.clients.AlbumsApiClient;
import com.epam.audio.streaming.album.microservice.exceptions.EntityAlreadyExistsException;
import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;
import com.epam.audio.streaming.songs.microservice.controllers.dto.SongFormDTO;
import com.epam.audio.streaming.songs.microservice.dao.repositories.ResourceRepository;
import com.epam.audio.streaming.songs.microservice.dao.repositories.SongRepository;
import com.epam.audio.streaming.songs.microservice.dao.repositories.StorageRepository;
import com.epam.audio.streaming.songs.microservice.dao.storages.ResourceStorage;
import com.epam.audio.streaming.songs.microservice.exceptions.minio.MinioBucketNotFoundException;
import com.epam.audio.streaming.songs.microservice.exceptions.mp3.Mp3ValidationException;
import com.epam.audio.streaming.songs.microservice.models.Resource;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.services.SongsService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.jms.ConnectionFactory;
import java.io.InputStream;
import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = {SongsController.class})
@AutoConfigureMockMvc( addFilters = false ) // disable security
public class SongsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StorageRepository storageRepository;

    @MockBean
    private ResourceRepository resourceRepository;

    @MockBean
    private ResourceStorage resourceStorage;

    @MockBean
    private AlbumsApiClient albumsApiClient;

    @MockBean
    private SongRepository songRepository;

    @MockBean
    private SongsService songsService;

    @MockBean
    private ConnectionFactory connectionFactory;

    @Autowired
    private SongsController songsController;

    private Song song = new Song(1L, "Name", 2001, "Notes", new Resource(), 1L);

    @Test
    public void getAllSongsTest() throws Exception {
        Mockito.when(songsService.getAllSongsInfo()).thenReturn(Arrays.asList(song));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllSongsWithServiceExceptionTest() throws Exception {
        Mockito.when(songsService.getAllSongsInfo()).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getSongTest() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream fileInputStream = classLoader.getResourceAsStream("SomeMp3File.mp3");

        Mockito.when(songsService.getSong(1L)).thenReturn(fileInputStream);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getNotExistedSongTest() throws Exception {
        Mockito.when(songsService.getSong(Mockito.anyLong())).thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getSongWithServiceExceptionTest() throws Exception {
        Mockito.when(songsService.getSong(Mockito.anyLong())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getSongsFromAlbumTest() throws Exception {
        Mockito.when(songsService.getSongsFromAlbum(1L)).thenReturn(Arrays.asList(song));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs/album/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getSongsFromNotExistedAlbumTest() throws Exception {
        Mockito.when(songsService.getSongsFromAlbum(1L)).thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs/album/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getSongsFromAlbumServiceExceptionTest() throws Exception {
        Mockito.when(songsService.getSongsFromAlbum(1L)).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/songs/album/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveSongTest() throws Exception {
        SongFormDTO songFormDTO = new SongFormDTO();
        songFormDTO.setName("Name");
        songFormDTO.setNotes("Notes");
        songFormDTO.setYear(2001);
        songFormDTO.setAlbumId(1L);
        songFormDTO.setFile(Mockito.mock(MultipartFile.class));

        Mockito.when(songsService.saveSong(Mockito.any(Song.class), Mockito.any())).thenReturn(song);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/songs")
                        .flashAttr("songFormDTO", songFormDTO)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveSongWrongFileExtensionTest() throws Exception {
        SongFormDTO songFormDTO = new SongFormDTO();
        songFormDTO.setName("Name");
        songFormDTO.setNotes("Notes");
        songFormDTO.setYear(2001);
        songFormDTO.setAlbumId(1L);
        songFormDTO.setFile(Mockito.mock(MultipartFile.class));

        Mockito.when(songsService.saveSong(Mockito.any(Song.class), Mockito.any())).thenThrow(Mp3ValidationException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/songs")
                        .flashAttr("songFormDTO", songFormDTO)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveSongAlreadyExistExtensionTest() throws Exception {
        SongFormDTO songFormDTO = new SongFormDTO();
        songFormDTO.setName("Name");
        songFormDTO.setNotes("Notes");
        songFormDTO.setYear(2001);
        songFormDTO.setAlbumId(1L);
        songFormDTO.setFile(Mockito.mock(MultipartFile.class));

        Mockito.when(songsService.saveSong(Mockito.any(Song.class), Mockito.any())).thenThrow(EntityAlreadyExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/songs")
                        .flashAttr("songFormDTO", songFormDTO)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveSongMinIoBucketExceptionTest() throws Exception {
        SongFormDTO songFormDTO = new SongFormDTO();
        songFormDTO.setName("Name");
        songFormDTO.setNotes("Notes");
        songFormDTO.setYear(2001);
        songFormDTO.setAlbumId(1L);
        songFormDTO.setFile(Mockito.mock(MultipartFile.class));

        Mockito.when(songsService.saveSong(Mockito.any(Song.class), Mockito.any())).thenThrow(MinioBucketNotFoundException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/songs")
                        .flashAttr("songFormDTO", songFormDTO)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void saveSongWithServiceExceptionTest() throws Exception {
        SongFormDTO songFormDTO = new SongFormDTO();

        Mockito.when(songsService.saveSong(Mockito.any(Song.class), Mockito.any())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/songs")
                        .flashAttr("songFormDTO", songFormDTO)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteSongTest() throws Exception {
        Mockito.doNothing().when(songsService).deleteSong(1L);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/songs/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteNotExistedSongTest() throws Exception {
        Mockito.doThrow(EntityNotExistsException.class).when(songsService).deleteSong(Mockito.anyLong());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/songs/3")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteSongWithServiceExceptionTest() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(songsService).deleteSong(Mockito.anyLong());

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/songs/1")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteAllSongsFromAlbumTest() throws Exception {
        Mockito.when(songsService.deleteSongsFromAlbum(1L)).thenReturn(Arrays.asList(1L, 2L, 3L));

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/songs/album/1/delete")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteAllSongsNotExistedAlbumTest() throws Exception {
        Mockito.when(songsService.deleteSongsFromAlbum(Mockito.anyLong())).thenThrow(EntityNotExistsException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/songs/album/5/delete")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteAllSongsFromAlbumWithServiceExceptionTest() throws Exception {
        Mockito.when(songsService.deleteSongsFromAlbum(Mockito.anyLong())).thenThrow(RuntimeException.class);

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/songs/album/5/delete")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
