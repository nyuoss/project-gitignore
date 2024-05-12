package com.example;


import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.example.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainIntegrationTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    private static final String resultsDirectoryFinal = resultPathString+"/results";

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    @AfterAll
    public static void cleanupResultsDirectory() throws IOException {
        // Delete contents of results directory and the directory itself
        deleteDirectory(Paths.get(resultsDirectoryFinal).toFile());
    }

    private static void deleteDirectory(File directory) throws IOException {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        Files.deleteIfExists(directory.toPath());
    }


    @Test
    @Order(1)
    public void testMainWithOverwriteFalse() throws IOException {
        String gitignoreFilePath =gitIgnorePathString;
        String startDirectory = "src/";
        String resultsDirectory = resultPathString;
        String overwriteFlag = "true";

        Main.main(new String[]{gitignoreFilePath, startDirectory, resultsDirectory, overwriteFlag});

        String consoleOutput = outContent.toString().trim();
        System.out.println(consoleOutput);
        assertTrue(consoleOutput.isEmpty(), "Expected empty console output");
    }
    @Test
    @Order(2)
    public void testMainWithValidArguments() throws IOException {
        String gitignoreFilePath =gitIgnorePathString;
        String startDirectory = "src/";
        String resultsDirectory = resultPathString;
        String overwriteFlag = "false";
        Main.main(new String[]{gitignoreFilePath, startDirectory, resultsDirectory, overwriteFlag});

        String consoleOutput = outContent.toString().trim();
        assertTrue(consoleOutput.isEmpty(), "Expected empty console output");

        // Count the number of files in the results directory
        long fileCount = Files.list(Paths.get(resultsDirectoryFinal))
                .filter(Files::isRegularFile)
                .count();
        System.out.println(fileCount);
        assertEquals(4, fileCount, "Expected 2 files in the 'results' subdirectory");

    }
}
