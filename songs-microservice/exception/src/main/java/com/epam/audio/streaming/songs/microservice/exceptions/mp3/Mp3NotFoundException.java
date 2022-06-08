package com.epam.audio.streaming.songs.microservice.exceptions.mp3;

import com.epam.audio.streaming.songs.microservice.exceptions.EntityNotExistsException;

public class Mp3NotFoundException extends EntityNotExistsException {
    public Mp3NotFoundException() {
        super();
    }

    public Mp3NotFoundException(String message) {
        super(message);
    }

    public Mp3NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public Mp3NotFoundException(Throwable cause) {
        super(cause);
    }

    protected Mp3NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
