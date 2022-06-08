package com.epam.audio.streaming.songs.microservice.dao.storages.implimentations;

import com.epam.audio.streaming.songs.microservice.dao.storages.ResourceStorage;
import com.epam.audio.streaming.songs.microservice.exceptions.minio.MinioBucketNotFoundException;
import com.epam.audio.streaming.songs.microservice.exceptions.minio.MinioClientException;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ResourceStorageImpl implements ResourceStorage {

    @Value("${minio.server.uri}")
    private String ENDPOINT = "http://localhost:9000";

    @Value("${minio.server.access.key}")
    private String ACCESS_KEY = "minioadmin";

    @Value("${minio.server.secret.key}")
    private String SECRET_KEY = "minioadmin";

    @Value("${minio.server.bucket.name}")
    private String BUCKET_NAME = "audio";

    private final MinioClient minioClient;

    public ResourceStorageImpl() {
        this.minioClient = MinioClient.builder()
                .endpoint(ENDPOINT)
                .credentials(ACCESS_KEY, SECRET_KEY)
                .build();
    }

    @Override
    public InputStream getFileByName(String name) throws MinioClientException {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())) {
                return minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(BUCKET_NAME)
                                .object(name)
                                .build()
                );
            } else {
                log.error("Bucket with name '{}' not found at the endpoint!.", BUCKET_NAME);
                throw new MinioBucketNotFoundException(String.format("Bucket with name '%s' not found at the endpoint!.", BUCKET_NAME));
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio error occurred! {}", e.getMessage());
            throw new MinioClientException("Some minio error has occurred! " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> saveFile(String name, InputStream audioData) throws MinioClientException {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())) {
                Map<String, String> result = new HashMap<>();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(name)
                        .stream(audioData, -1, 10485760)
                        .build());
                result.put("fileName", name);
                result.put("fileHash", Integer.toString(audioData.hashCode()));
                log.info("File '{}' has been saved in resource storage.", name);
                return result;
            } else {
                log.error("Bucket with name '{}' not found at the endpoint!.", BUCKET_NAME);
                throw new MinioBucketNotFoundException(String.format("Bucket with name '%s' not found at the endpoint!.", BUCKET_NAME));
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio error occurred! {}", e.getMessage());
            throw new MinioClientException("Some minio error has occurred! " + e.getMessage());
        }
    }

    @Override
    public void deleteFileByName(String name) throws MinioClientException {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())) {
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(name)
                        .build());
            } else {
                log.error("Bucket with name '{}' not found at the endpoint!.", BUCKET_NAME);
                throw new MinioBucketNotFoundException(String.format("Bucket with name '%s' not found at the endpoint!.", BUCKET_NAME));
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio error occurred! {}", e.getMessage());
            throw new MinioClientException("Some minio error has occurred! " + e.getMessage());
        }
    }

    @Override
    public Boolean fileExists(String name) throws MinioClientException {
        try {
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build())) {
                return minioClient.statObject(StatObjectArgs.builder().bucket(BUCKET_NAME).object(name).build()) != null;
            } else {
                log.error("Bucket with name '{}' not found at the endpoint!.", BUCKET_NAME);
                throw new MinioBucketNotFoundException(String.format("Bucket with name '%s' not found at the endpoint!.", BUCKET_NAME));
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            log.error("Minio error occurred! {}", e.getMessage());
            throw new MinioClientException("Some minio error has occurred! " + e.getMessage());
        }
    }

}
