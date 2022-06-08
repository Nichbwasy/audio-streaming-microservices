package com.epam.audio.streaming.songs.microservice.controllers.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SongDataDTO implements Serializable {
    @Size(min = 3, max = 200)
    private String name;

    @Min(1900)
    @Max(2022)
    @NotNull
    private Integer year;

    @Size(min = 5, max = 2000)
    private String notes;

    @Min(0)
    private Long albumId;

    @NotNull
    @ToString.Exclude
    private byte[] fileData;
}
