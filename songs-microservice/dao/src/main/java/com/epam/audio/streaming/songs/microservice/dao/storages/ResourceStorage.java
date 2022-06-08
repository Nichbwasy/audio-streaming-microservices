package com.epam.audio.streaming.songs.microservice.dao.storages;

import com.epam.audio.streaming.songs.microservice.exceptions.minio.MinioClientException;

import java.io.InputStream;
import java.util.Map;

public interface ResourceStorage {
    /**
     * Gets file binary data from resource storage.
     * @param name File name to get binary data.
     * @return File binary data.
     * @throws MinioClientException Throws when minio exception was occurred.
     */
    InputStream getFileByName(String name) throws MinioClientException;

    /**
     * Save a new file binary data with selected name.
     * @param name File name.
     * @param audioData Binary data to save.
     * @return Map which contains file name and file hash.
     * @throws MinioClientException Throws when minio exception was occurred.
     */
    Map<String, String> saveFile(String name, InputStream audioData) throws MinioClientException ;

    /**
     * Remove file from resource storage by name.
     * @param name File name.
     * @throws MinioClientException Throws when minio exception was occurred.
     */
    void deleteFileByName(String name) throws MinioClientException ;

    /**
     * Check file existence in storage by name.
     * @param name File name.
     * @return True if file exist in storage.
     * @throws MinioClientException Throws when minio exception was occurred.
     */
    Boolean fileExists(String name) throws MinioClientException ;
}
