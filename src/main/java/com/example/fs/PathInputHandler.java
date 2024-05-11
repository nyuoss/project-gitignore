package com.example.fs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PathInputHandler {
    private static final Pattern VALID_PATH_PATTERN = Pattern.compile("^[\\w\\-\\\\/:.\\s]+$");
    private Scanner scanner;
    private static final Logger LOGGER = Logger.getLogger(FileCreationHandler.class.getName());

    public PathInputHandler(Scanner scanner) {
        this.scanner = scanner;
    }

    public Path promptForGitignorePath(String message) {
        Path directory = null;
        while (directory == null) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                input = ".";
            }
            if (!isValidPath(input)) {
                System.out.println("Error: The path is invalid or contains illegal characters.");
                LOGGER.log(Level.WARNING, "Invalid path input: {0}", input);
                continue;
            }
            directory = Paths.get(input);
            if (!Files.isDirectory(directory)) {
                System.out.println("Error: The path is not a directory.");
                LOGGER.log(Level.WARNING, "Non-directory path provided: {0}", input);
                directory = null;
                continue;
            }
            Path potentialGitignore = directory.resolve(".gitignore");
            if (!Files.exists(potentialGitignore)) {
                System.out.println("Error: No .gitignore file found in the specified directory.");
                LOGGER.log(Level.WARNING, "Missing .gitignore file at {0}", directory);
                directory = null;
            } else {
                LOGGER.info("Found .gitignore file at: " + potentialGitignore);
                return potentialGitignore;
            }
        }
        return directory;
    }

    public Path promptForPath(String message, boolean mustBeDirectory) {
        Path path = null;
        while (path == null) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                path = Paths.get("").toAbsolutePath();
            } else {
                if (!isValidPath(input)) {
                    System.out.println("Error: The path is invalid or contains illegal characters.");
                    LOGGER.log(Level.WARNING, "Invalid path input: {0}", input);
                    continue;
                }
                path = Paths.get(input);
                if (!Files.exists(path) || (mustBeDirectory && !Files.isDirectory(path))) {
                    System.out.println("Error: The path does not exist or is not a directory.");
                    LOGGER.log(Level.SEVERE, "Invalid or non-existent path: {0}", path);
                    path = null;
                } else {
                    LOGGER.info("Valid path accepted: " + path);

                }
            }
        }
        return path;
    }

    private boolean isValidPath(String path) {
        boolean isValid = VALID_PATH_PATTERN.matcher(path).matches();
        if (!isValid) {
            LOGGER.log(Level.SEVERE, "Path validation failed for: {0}", path);
        }
        return isValid;
    }

}
