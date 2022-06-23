package com.epam.audio.streaming.artists.microservice.exceptions.genres;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;

public class GenreNameValidationException extends EntityValidationException {
    public GenreNameValidationException() {
        super();
    }

    public GenreNameValidationException(String message) {
        super(message);
    }

    public GenreNameValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenreNameValidationException(Throwable cause) {
        super(cause);
    }

    protected GenreNameValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
