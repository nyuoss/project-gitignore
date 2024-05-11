package com.example.fs;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class PathInputHandler {
    private static final Pattern VALID_PATH_PATTERN = Pattern.compile("^[\\w\\-\\\\/:.\\s]+$");
    private Scanner scanner;

    public PathInputHandler(Scanner scanner) {
        this.scanner = scanner;
        AnsiConsole.systemInstall(); 
    }

    public Path promptForGitignorePath(String message) {
        Path directory = null;
        while (directory == null) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a(message).reset());
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                input = ".";
            }
            if (!isValidPath(input)) {
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Error: The path is invalid or contains illegal characters.").reset());
                continue;
            }
            directory = Paths.get(input);
            if (!Files.isDirectory(directory)) {
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Error: The path is not a directory.").reset());
                directory = null;
                continue;
            }
            Path potentialGitignore = directory.resolve(".gitignore");
            if (!Files.exists(potentialGitignore)) {
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Error: No .gitignore file found in the specified directory.").reset());
                directory = null; 
            } else {
                System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("Gitignore file found: " + potentialGitignore).reset());
                return potentialGitignore;
            }
        }
        return directory;
    }

    public Path promptForPath(String message, boolean mustBeDirectory) {
        Path path = null;
        while (path == null) {
            System.out.print(Ansi.ansi().fg(Ansi.Color.CYAN).a(message).reset());
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                path = Paths.get("").toAbsolutePath(); 
            } else {
                if (!isValidPath(input)) {
                    System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Error: The path is invalid or contains illegal characters.").reset());
                    continue;
                }
                path = Paths.get(input);
                if (!Files.exists(path) || (mustBeDirectory && !Files.isDirectory(path))) {
                    System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Error: The path does not exist or is not a directory.").reset());
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
