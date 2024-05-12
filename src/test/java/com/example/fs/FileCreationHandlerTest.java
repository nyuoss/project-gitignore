package com.example.fs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class FileCreationHandlerTest {

    private FileCreationHandler fileCreationHandler;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        fileCreationHandler = new FileCreationHandler();
        tempDir = Files.createTempDirectory("testFileCreationHandler");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Recursively delete the temp directory to clean up after tests
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void testHandleFileCreationNewFile() throws IOException {
        Path expectedPath = tempDir.resolve("results").resolve("log.txt");

        Path resultPath = fileCreationHandler.handleFileCreation(tempDir, "log.txt", false);

        assertTrue(Files.exists(resultPath), "File should be created");
        assertEquals(expectedPath, resultPath, "Paths should match");
    }

    @Test
    void testHandleFileCreationOverwrite() throws IOException {
        Path resultsDir = tempDir.resolve("results");
        Path file = resultsDir.resolve("log.txt");
        Files.createDirectories(resultsDir);
        Files.createFile(file); // Create the file to be overwritten

        Path resultPath = fileCreationHandler.handleFileCreation(tempDir, "log.txt", true);

        assertTrue(Files.exists(resultPath), "File should exist");
        assertTrue(Files.isRegularFile(resultPath), "Should be a regular file");
        assertEquals(file, resultPath, "File path should match the expected path");
    }

    @Test
    void testHandleFileCreationNoOverwriteNewVersion() throws IOException {
        Path resultsDir = tempDir.resolve("results");
        Path file = resultsDir.resolve("log.txt");
        Files.createDirectories(resultsDir);
        Files.createFile(file); // Create the file that should not be overwritten

        Path resultPath = fileCreationHandler.handleFileCreation(tempDir, "log.txt", false);

        assertNotEquals(file, resultPath, "New file version should have a different path");
        assertTrue(resultPath.toString().contains("log(1).txt"), "New version should follow naming convention");
    }

    @Test
    void testDirectoryCreationFailure() throws IOException {
        Path inaccessibleDir = tempDir.resolve("inaccessibleDir");
        Files.createDirectory(inaccessibleDir);
        inaccessibleDir.toFile().setReadOnly();

        assertThrows(IOException.class, () -> fileCreationHandler.handleFileCreation(inaccessibleDir, "log.txt", false),
                "Should throw IOException due to directory access restriction");
    }
}
