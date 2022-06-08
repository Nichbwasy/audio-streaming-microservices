package com.epam.audio.streaming.artists.microservice.exceptions.artists;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityNotExistsException;

public class ArtistNotExistException extends EntityNotExistsException {
    public ArtistNotExistException() {
        super();
    }

    public ArtistNotExistException(String message) {
        super(message);
    }

    public ArtistNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArtistNotExistException(Throwable cause) {
        super(cause);
    }

    protected ArtistNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
