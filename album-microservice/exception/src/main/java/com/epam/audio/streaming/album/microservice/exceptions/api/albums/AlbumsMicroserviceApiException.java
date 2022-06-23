package com.epam.audio.streaming.album.microservice.exceptions.api.albums;

import com.epam.audio.streaming.album.microservice.exceptions.api.MicroserviceApiException;

public class AlbumsMicroserviceApiException extends MicroserviceApiException {
    public AlbumsMicroserviceApiException() {
        super();
    }

    public AlbumsMicroserviceApiException(String message) {
        super(message);
    }

    public AlbumsMicroserviceApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumsMicroserviceApiException(Throwable cause) {
        super(cause);
    }

    protected AlbumsMicroserviceApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
