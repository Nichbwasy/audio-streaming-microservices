package com.epam.audio.streaming.songs.microservice.exceptions.mp3;

import com.epam.audio.streaming.songs.microservice.exceptions.EntityValidationException;

public class Mp3ValidationException extends EntityValidationException {
    public Mp3ValidationException() {
        super();
    }

    public Mp3ValidationException(String message) {
        super(message);
    }

    public Mp3ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Mp3ValidationException(Throwable cause) {
        super(cause);
    }

    protected Mp3ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
