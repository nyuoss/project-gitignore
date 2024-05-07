package com.example;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.fs.FileSystemWalker;

public class Main {
    public static void main(String[] args) {
        Path gitignorePath = Paths
                .get("C:/Users/hao/Desktop/Master/MS I/Software Engineering I/Wednesday-Fall2023-Team-6/.gitignore");
        Path startPath = Paths.get("C:/Users/hao/Desktop/Master/MS I/Software Engineering I/Wednesday-Fall2023-Team-6");
        Path humanReadablePath = Paths.get("results/Human Readable Summary.txt");
        Path machineReadablePath = Paths.get("results/Machine Readable Summary.txt");

        FileSystemWalker walker = new FileSystemWalker(gitignorePath, humanReadablePath, machineReadablePath);
        walker.walkFileTree(startPath);
    }
}
