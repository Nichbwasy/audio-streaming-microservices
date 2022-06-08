package com.epam.audio.streaming.songs.microservice.exceptions;

public class EntityNotExistsException extends RuntimeException{
    public EntityNotExistsException() {
        super();
    }

    public EntityNotExistsException(String message) {
        super(message);
    }

    public EntityNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotExistsException(Throwable cause) {
        super(cause);
    }

    protected EntityNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
