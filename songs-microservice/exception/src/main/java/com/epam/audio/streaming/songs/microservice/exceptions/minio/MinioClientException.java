package com.epam.audio.streaming.songs.microservice.exceptions.minio;

public class MinioClientException extends RuntimeException{
    public MinioClientException() {
        super();
    }

    public MinioClientException(String message) {
        super(message);
    }

    public MinioClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinioClientException(Throwable cause) {
        super(cause);
    }

    protected MinioClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
