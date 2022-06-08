package com.epam.audio.streaming.songs.microservice.exceptions.minio;

public class MinioBucketNotFoundException extends RuntimeException {
    public MinioBucketNotFoundException() {
        super();
    }

    public MinioBucketNotFoundException(String message) {
        super(message);
    }

    public MinioBucketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioBucketNotFoundException(Throwable cause) {
        super(cause);
    }

    protected MinioBucketNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
