package com.epam.audio.streaming.album.microservice.exceptions.album;

import com.epam.audio.streaming.album.microservice.exceptions.EntityNotExistsException;

public class AlbumNotExistsException extends EntityNotExistsException {
    public AlbumNotExistsException() {
        super();
    }

    public AlbumNotExistsException(String message) {
        super(message);
    }

    public AlbumNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumNotExistsException(Throwable cause) {
        super(cause);
    }

    protected AlbumNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
