package com.epam.audio.streaming.album.microservice.controllers.dto;

import com.epam.audio.streaming.album.microservice.models.Album;
import com.epam.audio.streaming.artists.microservice.models.Artist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumArtistsDTO {
    private Album album;
    private Artist artist;
}
