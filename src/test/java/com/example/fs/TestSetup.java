package com.example.fs;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.example.Constants.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({FileMatcherTest.class, FileSystemWalkerTest.class})
public class TestSetup {


    @BeforeClass
    public static void setupTestEnvironment() throws Exception {
        if (!Files.exists(testPath)) {
            Files.createDirectories(testPath);
        }
        createEmptyOutputFiles();
    }


    private static void createEmptyOutputFiles() throws Exception {
        Path humanReadablePath = testPath.resolve("human_readable.txt");
        Path machineReadablePath = testPath.resolve("machine_readable.json");
        Files.createFile(humanReadablePath);
        Files.createFile(machineReadablePath);
    }

    @AfterClass
    public static void cleanupTestEnvironment() {
        try {
            System.out.println("Cleanup Test Environment...");

            // Delete files
            Files.deleteIfExists(humanReadablePath);
            Files.deleteIfExists(machineReadablePath);


            System.out.println("Cleanup completed successfully");
        } catch (IOException e) {
            System.err.println("Error occurred during cleanup: " + e.getMessage());
        }
    }
}