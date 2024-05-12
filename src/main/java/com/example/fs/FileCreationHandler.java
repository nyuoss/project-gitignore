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

//    private boolean confirmOverwrite(String fileName) {
//        System.out.print(fileName + " file already exists. Do you want to overwrite it? (y/n): ");
//        while (true) {
//            String response = scanner.nextLine().trim().toLowerCase();
//            if (response.equals("y")) {
//                logger.finest("User confirmed to overwrite file: " + fileName);
//                return true;
//            } else if (response.equals("n")) {
//                logger.finest("User declined to overwrite file: " + fileName);
//                return false;
//            } else {
//                logger.warning("Invalid response received: " + response);
//                System.out.println("Invalid response. Please enter 'y' for yes or 'n' for no.");
//            }
//        }
//    }
}
