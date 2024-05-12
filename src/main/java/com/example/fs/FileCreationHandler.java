package com.example.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FileCreationHandler {
    private static final Logger logger = Logger.getLogger(FileCreationHandler.class.getName());

    public FileCreationHandler() {
    }

    public Path handleFileCreation(Path defaultPath, String defaultFileName, boolean overwrite) throws IOException {
        logger.finest("Attempting to handle file creation for: " + defaultFileName);
        Path resultsPath = defaultPath.resolve("results");
        try {
            Files.createDirectories(resultsPath);
            logger.info("Directories created/verified at: " + resultsPath);
        } catch (IOException e) {
            logger.severe("Failed to create directories: " + resultsPath);
            throw e;
        }

        Path path = resultsPath.resolve(defaultFileName);

        if (Files.exists(path)) {
            logger.warning("File already exists: " + path);
            if (overwrite) {
                Files.deleteIfExists(path);
                Files.createFile(path);
                logger.info("File overwritten: " + path);
            } else {
                path = createNewVersionOfFile(path);
                logger.info("New file version created: " + path);
            }
        } else {
            Files.createFile(path);
            logger.info("New file version created: " + path);
        }
        return path;
    }

    private Path createNewVersionOfFile(Path originalPath) throws IOException {
        int counter = 1;
        Path newPath = originalPath;
        String fileName = originalPath.getFileName().toString();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        while (Files.exists(newPath)) {
            newPath = originalPath.getParent().resolve(baseName + "(" + counter + ")" + extension);
            counter++;
        }
        Files.createFile(newPath);
        logger.finest("New version of file created: " + newPath);
        return newPath;
    }

}
