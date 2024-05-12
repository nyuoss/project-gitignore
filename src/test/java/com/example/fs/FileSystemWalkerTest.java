package com.example.fs;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Files;

import static com.example.Constants.*;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileSystemWalkerTest {

    private FileSystemWalker walker;

    @Before
    public void setUp() {
        walker = new FileSystemWalker(gitIgnorePath,
                humanReadablePath, machineReadablePath);
    }

    @Test
    public void test1_WalkFileTree() {
        walker.walkFileTree(startPath);
        assertTrue("Human readable output should exist", Files.exists(humanReadablePath));
        assertTrue("Machine readable output should exist", Files.exists(machineReadablePath));
    }

    @Test
    public void test2_ExecutionTime() throws IOException {
        String jsonContent = new String(Files.readAllBytes(machineReadablePath));
        System.out.println("JSON Content: " + jsonContent);  // Debug output
        assertTrue("Execution time should be recorded", jsonContent.contains("executionTime"));
    }

    @Test
    public void test3_FileIgnore() throws IOException {
        String content = new String(Files.readAllBytes(humanReadablePath));
        System.out.println("Debug - Human Readable Content: " + content);
        boolean hasIgnoredFiles = content.contains("[EXCLUDED by");
        System.out.println("Debug - Contains ignored files: " + hasIgnoredFiles);
        assertTrue("Ignored files should be mentioned in the output", hasIgnoredFiles);
    }

    @Test
    public void test4_FileInclusion() throws IOException {
        String content = new String(Files.readAllBytes(humanReadablePath));
        System.out.println("Debug - Human Readable Content: " + content);
        assertTrue("Included files should be mentioned in the output", content.contains("[INCLUDED]"));
        Files.deleteIfExists(humanReadablePath);
        Files.deleteIfExists(machineReadablePath);
    }
}
