package com.epam.audio.streaming.songs.microservice.exceptions.api.songs;

import com.epam.audio.streaming.album.microservice.exceptions.api.MicroserviceApiException;

public class SongsMicroserviceApiException extends MicroserviceApiException {
    public SongsMicroserviceApiException() {
        super();
    }

    public SongsMicroserviceApiException(String message) {
        super(message);
    }

    public SongsMicroserviceApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public SongsMicroserviceApiException(Throwable cause) {
        super(cause);
    }

    protected SongsMicroserviceApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
