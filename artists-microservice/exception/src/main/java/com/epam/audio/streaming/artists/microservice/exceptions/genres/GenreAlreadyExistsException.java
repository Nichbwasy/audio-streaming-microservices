package com.epam.audio.streaming.artists.microservice.exceptions.genres;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityAlreadyExistsException;

public class GenreAlreadyExistsException extends EntityAlreadyExistsException {
    public GenreAlreadyExistsException() {
    }

    public GenreAlreadyExistsException(String message) {
        super(message);
    }

    public GenreAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenreAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public GenreAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
