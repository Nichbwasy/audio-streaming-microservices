package com.epam.audio.streaming.artists.microservice.exceptions.artists;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;


public class ArtistGenresValidationException extends EntityValidationException {
    public ArtistGenresValidationException() {
        super();
    }

    public ArtistGenresValidationException(String message) {
        super(message);
    }

    public ArtistGenresValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArtistGenresValidationException(Throwable cause) {
        super(cause);
    }

    protected ArtistGenresValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
