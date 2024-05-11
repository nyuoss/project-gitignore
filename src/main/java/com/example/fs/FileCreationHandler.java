package com.example.fs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class FileCreationHandler {
    private Scanner scanner;

    public FileCreationHandler(Scanner scanner) {
        this.scanner = scanner;
        AnsiConsole.systemInstall(); 
    }

    public Path handleFileCreation(Path defaultPath, String defaultFileName) throws IOException {
        Path resultsPath = defaultPath.resolve("results");
        Files.createDirectories(resultsPath); 

        Path path = resultsPath.resolve(defaultFileName);
        
        if (Files.exists(path)) {
            if (confirmOverwrite(defaultFileName)) {
                Files.deleteIfExists(path);
                Files.createFile(path);
                System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("File overwritten successfully: " + path).reset());
            } else {
                path = createNewVersionOfFile(path);
                System.out.println(Ansi.ansi().fg(Ansi.Color.YELLOW).a("New version created: " + path).reset());
            }
        } else {
            Files.createFile(path);
            System.out.println(Ansi.ansi().fg(Ansi.Color.GREEN).a("File created successfully: " + path).reset());
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
        System.out.print(Ansi.ansi().fg(Ansi.Color.RED).a(fileName + " file already exists. Do you want to overwrite it? (y/n): ").reset());
        while (true) {
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            } else {
                System.out.println(Ansi.ansi().fg(Ansi.Color.RED).a("Invalid response. Please enter 'y' for yes or 'n' for no.").reset());
            }
        }
    }
}
