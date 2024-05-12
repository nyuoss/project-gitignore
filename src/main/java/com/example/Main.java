package com.example;

import com.example.fs.FileCreationHandler;
import com.example.fs.FileSystemWalker;
import com.example.gui.GUI;
import java.awt.EventQueue;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            // Launch GUI if no arguments are provided
            EventQueue.invokeLater(() -> {
                GUI gui = new GUI();
                gui.setVisible(true);
            });
        } else {
            // Process command line arguments
            processCommandLine(args);
        }
    }

    private static void processCommandLine(String[] args) {
        try {
            if (args.length < 4) {
                System.err.println("Usage: java -jar GitignoreVerifier.jar <gitignoreFilePath> <startDirectory> <resultsDirectory> <overwrite>");
                return;
            }

            Path gitignorePath = Paths.get(args[0]);
            Path startPath = Paths.get(args[1]);
            Path resultsDirectory = Paths.get(args[2]);
            boolean overwrite = Boolean.parseBoolean(args[3]);

            if (!validatePaths(gitignorePath, startPath, resultsDirectory)) {
                return;
            }

            FileCreationHandler fileCreationHandler = new FileCreationHandler();
            Path humanReadablePath = fileCreationHandler.handleFileCreation(resultsDirectory, "Human Readable Summary.txt", overwrite);
            Path machineReadablePath = fileCreationHandler.handleFileCreation(resultsDirectory, "Machine Readable Summary.json", overwrite);

            FileSystemWalker walker = new FileSystemWalker(gitignorePath, humanReadablePath, machineReadablePath);
            walker.walkFileTree(startPath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean validatePaths(Path gitignorePath, Path startPath, Path resultsDirectory) {
        if (!gitignorePath.toFile().exists()) {
            System.err.println("No .gitignore file found at specified path: " + gitignorePath);
            return false;
        }
        if (!startPath.toFile().isDirectory()) {
            System.err.println("Start path is not a directory: " + startPath);
            return false;
        }
        if (!resultsDirectory.toFile().isDirectory()) {
            System.err.println("Results directory path is not a directory: " + resultsDirectory);
            return false;
        }
        return true;
    }
}