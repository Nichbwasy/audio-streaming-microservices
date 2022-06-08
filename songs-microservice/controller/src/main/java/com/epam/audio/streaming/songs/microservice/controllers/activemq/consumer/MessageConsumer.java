package com.epam.audio.streaming.songs.microservice.controllers.activemq.consumer;

import com.epam.audio.streaming.songs.microservice.controllers.dto.SongDataDTO;
import com.epam.audio.streaming.songs.microservice.controllers.dto.SongFormDTO;
import com.epam.audio.streaming.songs.microservice.models.Song;
import com.epam.audio.streaming.songs.microservice.services.SongsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class MessageConsumer {

    @Autowired
    private SongsService songsService;

    @JmsListener(destination = "received-songs-queue")
    public void messageListener(SongDataDTO songDataDTO) {
        try {
            log.info("New song has been received: {}.", songDataDTO);
            Song song = new Song(songDataDTO.getName(), songDataDTO.getYear(), songDataDTO.getNotes());
            songsService.saveSong(song, songDataDTO.getFileData());
        } catch (Exception e) {
            log.error("Something went wrong while saving a new song! {}", e.getMessage());
        }
    }
}
