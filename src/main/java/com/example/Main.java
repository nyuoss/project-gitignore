package com.example;

import com.example.fs.FileCreationHandler;
import com.example.fs.FileSystemWalker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            // Check if sufficient arguments are provided
            if (args.length < 4) {
                System.err.println("Usage: java -jar GitignoreVerifier.jar <gitignoreFilePath> <startDirectory> <resultsDirectory> <overwrite>");
                return;
            }

            // Assuming args[0] is the direct path to the .gitignore file
            Path gitignorePath = Paths.get(args[0]);
            if (!gitignorePath.toFile().exists()) {
                System.err.println("No .gitignore file found at specified path: " + gitignorePath);
                return;
            }

            Path startPath = Paths.get(args[1]);
            if (!startPath.toFile().isDirectory()) {
                System.err.println("Start path is not a directory: " + startPath);
                return;
            }

            Path resultsDirectory = Paths.get(args[2]);
            if (!resultsDirectory.toFile().isDirectory()) {
                System.err.println("Results directory path is not a directory: " + resultsDirectory);
                return;
            }

            boolean overwrite = Boolean.parseBoolean(args[3]); // Parse the overwrite flag from command-line arguments

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
}
