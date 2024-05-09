package com.example.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class FileCreationHandler {
    private Scanner scanner;

    public FileCreationHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public Path handleFileCreation(Path defaultPath, String defaultFileName) throws IOException {

        Path resultsPath = defaultPath.resolve("results");
        Files.createDirectories(resultsPath);

        Path path = resultsPath.resolve(defaultFileName);
        
        if (Files.exists(path)) {
            if (confirmOverwrite(defaultFileName)) {
                Files.deleteIfExists(path);
                Files.createFile(path);
            } else {
                path = createNewVersionOfFile(path);
            }
        } else {
            Files.createFile(path);
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
        return newPath;
    }

    private boolean confirmOverwrite(String fileName) {
        System.out.print(fileName + " file already exists. Do you want to overwrite it? (y/n): ");
        while (true) {
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid response. Please enter 'y' for yes or 'n' for no.");
            }
        }
    }
}
