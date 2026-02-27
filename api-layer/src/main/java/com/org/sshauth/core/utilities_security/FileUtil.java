package com.org.sshauth.core.utilities_security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for file operations.
 */
@Slf4j
@Component
public class FileUtil {

    /**
     * Read file content as string.
     */
    public String readFileAsString(String filePath) {
        try {
            log.debug("Reading file: {}", filePath);
            return new String(Files.readAllBytes(Paths.get(filePath)));

        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
            throw new RuntimeException("Failed to read file", e);
        }
    }

    /**
     * Write content to file.
     */
    public void writeToFile(String filePath, String content) {
        try {
            log.debug("Writing to file: {}", filePath);
            Files.write(Paths.get(filePath), content.getBytes());

        } catch (IOException e) {
            log.error("Error writing to file: {}", filePath, e);
            throw new RuntimeException("Failed to write to file", e);
        }
    }

    /**
     * Check if file exists.
     */
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * Delete a file.
     */
    public void deleteFile(String filePath) {
        try {
            log.debug("Deleting file: {}", filePath);
            Files.deleteIfExists(Paths.get(filePath));

        } catch (IOException e) {
            log.error("Error deleting file: {}", filePath, e);
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    /**
     * Create directory if not exists.
     */
    public void createDirectoryIfNotExists(String directoryPath) {
        try {
            log.debug("Creating directory if not exists: {}", directoryPath);
            Files.createDirectories(Paths.get(directoryPath));

        } catch (IOException e) {
            log.error("Error creating directory: {}", directoryPath, e);
            throw new RuntimeException("Failed to create directory", e);
        }
    }

    /**
     * Get file size in bytes.
     */
    public long getFileSize(String filePath) {
        try {
            return Files.size(Paths.get(filePath));

        } catch (IOException e) {
            log.error("Error getting file size: {}", filePath, e);
            throw new RuntimeException("Failed to get file size", e);
        }
    }
}
