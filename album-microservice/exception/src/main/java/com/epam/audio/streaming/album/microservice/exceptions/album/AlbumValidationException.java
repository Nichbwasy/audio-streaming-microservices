package com.epam.audio.streaming.album.microservice.exceptions.album;

import com.epam.audio.streaming.album.microservice.exceptions.EntityValidationException;

public class AlbumValidationException extends EntityValidationException {
    public AlbumValidationException() {
        super();
    }

    public AlbumValidationException(String message) {
        super(message);
    }

    public AlbumValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumValidationException(Throwable cause) {
        super(cause);
    }

    protected AlbumValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
