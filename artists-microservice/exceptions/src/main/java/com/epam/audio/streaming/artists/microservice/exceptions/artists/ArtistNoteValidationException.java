package com.epam.audio.streaming.artists.microservice.exceptions.artists;

import com.epam.audio.streaming.artists.microservice.exceptions.EntityValidationException;


public class ArtistNoteValidationException extends EntityValidationException {
    public ArtistNoteValidationException() {
        super();
    }

    public ArtistNoteValidationException(String message) {
        super(message);
    }

    public ArtistNoteValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArtistNoteValidationException(Throwable cause) {
        super(cause);
    }

    protected ArtistNoteValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
