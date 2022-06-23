package com.epam.audio.streaming.artists.microservice.exceptions.artists;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;

public class ArtistNameValidationException extends EntityValidationException {
    public ArtistNameValidationException() {
        super();
    }

    public ArtistNameValidationException(String message) {
        super(message);
    }

    public ArtistNameValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArtistNameValidationException(Throwable cause) {
        super(cause);
    }

    protected ArtistNameValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
