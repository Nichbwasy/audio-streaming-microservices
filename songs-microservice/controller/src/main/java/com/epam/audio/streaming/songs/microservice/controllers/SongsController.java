package com.epam.audio.streaming.songs.microservice.controllers;

import com.epam.audio.streaming.songs.microservice.controllers.dto.SongDataDTO;
import com.epam.audio.streaming.songs.microservice.controllers.dto.SongFormDTO;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.services.SongsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/songs")
public class SongsController {

    @Autowired
    private SongsService songsService;

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() throws IOException {
        List<Song> songs = songsService.getAllSongsInfo();
        return ResponseEntity.ok().body(songs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getSong(@PathVariable Long id) throws IOException {
        return ResponseEntity.ok().body(IOUtils.toByteArray(songsService.getSong(id)));
    }

    @GetMapping("/album/{albumId}")
    public ResponseEntity<List<Song>> getSongsFromAlbum(@PathVariable Long albumId) throws IOException {
        List<Song> songs = songsService.getSongsFromAlbum(albumId);
        return ResponseEntity.ok().body(songs);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> saveSong(@ModelAttribute SongFormDTO formData) throws IOException {
        Map<String, String> result = new HashMap<>();
        Song song = new Song(formData.getName(), formData.getYear(), formData.getNotes(), formData.getAlbumId());
        songsService.saveSong(song, formData.getFile().getBytes());
        result.put("message", "New song data has been sent.");
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id) {
        songsService.deleteSong(id);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/album/{albumId}/delete")
    public ResponseEntity<List<Long>> deleteAllSongsFromAlbum(@PathVariable Long albumId) {
        List<Long> ids = songsService.deleteSongsFromAlbum(albumId);
        return ResponseEntity.ok().body(ids);
    }
}
