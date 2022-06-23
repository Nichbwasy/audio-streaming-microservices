package com.epam.audio.streaming.songs.microservice.exceptions.api;

public class MicroserviceApiException extends RuntimeException {
    public MicroserviceApiException() {
        super();
    }

    public MicroserviceApiException(String message) {
        super(message);
    }

    public MicroserviceApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public MicroserviceApiException(Throwable cause) {
        super(cause);
    }

    protected MicroserviceApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
