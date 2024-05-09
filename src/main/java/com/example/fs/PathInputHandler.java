package com.example.fs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PathInputHandler {
    private static final Pattern VALID_PATH_PATTERN = Pattern.compile("^[\\w\\-\\\\/:.\\s]+$");
    private Scanner scanner;

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
                continue;
            }
            directory = Paths.get(input);
            if (!Files.isDirectory(directory)) {
                System.out.println("Error: The path is not a directory.");
                directory = null;
                continue;
            }
            Path potentialGitignore = directory.resolve(".gitignore");
            if (!Files.exists(potentialGitignore)) {
                System.out.println("Error: No .gitignore file found in the specified directory.");
                directory = null; 
            } else {
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
                    continue;
                }
                path = Paths.get(input);
                if (!Files.exists(path) || (mustBeDirectory && !Files.isDirectory(path))) {
                    System.out.println("Error: The path does not exist or is not a directory.");
                    path = null; 
                }
            }
        }
        return path;
    }

    private boolean isValidPath(String path) {
        return VALID_PATH_PATTERN.matcher(path).matches();
    }
}
