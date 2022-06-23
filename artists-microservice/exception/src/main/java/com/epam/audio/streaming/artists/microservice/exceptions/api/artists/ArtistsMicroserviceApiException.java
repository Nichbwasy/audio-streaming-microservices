package com.epam.audio.streaming.artists.microservice.exceptions.api.artists;

import com.epam.audio.streaming.artists.microservice.exceptions.api.MicroserviceApiException;

public class ArtistsMicroserviceApiException extends MicroserviceApiException {
    public ArtistsMicroserviceApiException() {
        super();
    }

    public ArtistsMicroserviceApiException(String message) {
        super(message);
    }

    public ArtistsMicroserviceApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArtistsMicroserviceApiException(Throwable cause) {
        super(cause);
    }

    protected ArtistsMicroserviceApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
