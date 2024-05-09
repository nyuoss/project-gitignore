package com.example;

import com.example.fs.FileCreationHandler;
import com.example.fs.FileSystemWalker;
import com.example.fs.PathInputHandler;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            PathInputHandler pathInputHandler = new PathInputHandler(scanner);
            FileCreationHandler fileCreationHandler = new FileCreationHandler(scanner);

            Path gitignorePath = pathInputHandler.promptForGitignorePath("Enter the directory path for the .gitignore file or press 'Enter' to use the current directory:");
            Path startPath = pathInputHandler.promptForPath("Enter the start directory path or press Enter to use the current directory:", true);
            Path resultsDirectory = pathInputHandler.promptForPath("Enter the results directory or press Enter to use the 'results' folder in the current directory:", true);

            Path humanReadablePath = fileCreationHandler.handleFileCreation(resultsDirectory, "Human Readable Summary.txt");
            Path machineReadablePath = fileCreationHandler.handleFileCreation(resultsDirectory, "Machine Readable Summary.json");

            FileSystemWalker walker = new FileSystemWalker(gitignorePath, humanReadablePath, machineReadablePath);
            walker.walkFileTree(startPath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
