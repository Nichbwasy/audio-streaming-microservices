package com.epam.audio.streaming.album.microservice.exceptions.album;

import com.epam.audio.streaming.album.microservice.exceptions.EntityAlreadyExistsException;

public class AlbumAlreadyExistsException extends EntityAlreadyExistsException {
    public AlbumAlreadyExistsException() {
        super();
    }

    public AlbumAlreadyExistsException(String message) {
        super(message);
    }

    public AlbumAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlbumAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected AlbumAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
