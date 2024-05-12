package com.example.fs;


import com.example.rules.Rule;
import com.example.rules.RuleResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FileSystemWalkerTest {

    private FileSystemWalker walker;

    @BeforeEach
    public void setUp() {
        walker = new FileSystemWalker(gitIgnorePath, humanReadablePath, machineReadablePath);
    }

    @Test
    @Order(1)
    public void walkFileTreeTest() throws IOException {
        walker.walkFileTree(startPath);
        assertTrue(Files.exists(humanReadablePath), "Human readable output should exist");
        assertTrue(Files.exists(machineReadablePath), "Machine readable output should exist");
    }

    @Test
    @Order(2)
    public void executionTimeTest() throws IOException {
        String jsonContent = new String(Files.readAllBytes(machineReadablePath));
        System.out.println("JSON Content: " + jsonContent);  // Debug output
        assertTrue(jsonContent.contains("executionTime"), "Execution time should be recorded");
    }

    @Test
    @Order(3)
    public void fileIgnoreTest() throws IOException {
        String content = new String(Files.readAllBytes(humanReadablePath));
        System.out.println("Debug - Human Readable Content: " + content);
        boolean hasIgnoredFiles = content.contains("[EXCLUDED by");
        System.out.println("Debug - Contains ignored files: " + hasIgnoredFiles);
        assertTrue(hasIgnoredFiles, "Ignored files should be mentioned in the output");
    }

    @Test
    @Order(4)
    public void fileInclusionTest() throws IOException {
        String content = new String(Files.readAllBytes(humanReadablePath));
        System.out.println("Debug - Human Readable Content: " + content);
        assertTrue(content.contains("[INCLUDED]"), "Included files should be mentioned in the output");
        Files.deleteIfExists(humanReadablePath);
        Files.deleteIfExists(machineReadablePath);
    }

    @Test
    @Order(5)
    public void emptyDirectoryTest() throws IOException {
        Path emptyTestPath = Files.createTempDirectory("emptyTestDir");
        walker.walkFileTree(emptyTestPath);
        assertTrue(Files.exists(humanReadablePath), "Human readable output for empty directory should exist");
        String humanContent = new String(Files.readAllBytes(humanReadablePath));
        System.out.println("Debug - Human Readable Content: " + humanContent);

        assertTrue(humanContent.contains("Total Files Processed: 1"), "Human readable content should indicate one file processed");
        Files.deleteIfExists(emptyTestPath);
    }

    @Test
    @Order(6)
    public void nonExistentStartPathTest() {
        Path nonExistentPath = Paths.get("some/nonexistent/directory");
        Exception exception = assertThrows(IOException.class, () -> walker.walkFileTree(nonExistentPath));
        assertTrue(exception.getMessage().contains("nonexistent"), "Exception message should mention nonexistent path");
    }

    @Test
    @Order(7)
    public void testProcessPath() throws IOException {
        Path testPath = Paths.get("some/existing/file.txt"); // Assume this path exists for the test
        // Mock or simulate fileMatcher response for testing
        FileMatcher fileMatcher = mock(FileMatcher.class);
        when(fileMatcher.isIgnored(testPath)).thenReturn(new RuleResult(false, new ArrayList<>())); // Assume file is not ignored

        // Simulate processing of path
        ObjectNode includedNode = new ObjectMapper().createObjectNode();
        ObjectNode ignoredNode = new ObjectMapper().createObjectNode();
        walker.processPath(testPath, includedNode, ignoredNode);

        // Check results
        assertTrue(includedNode.has("some/existing/file.txt"), "File should be included");
        assertFalse(ignoredNode.has("some/existing/file.txt"), "File should not be ignored");
    }

    @Test
    @Order(8)
    public void testHandleIgnoredFiles() {
        Path ignoredPath = Paths.get("ignored/file.txt"); // Assume this path for testing
        RuleResult mockResult = mock(RuleResult.class);
        List<Rule> rules = Arrays.asList(new Rule("*.txt"), new Rule("ignored/*"));
        when(mockResult.getMatchingRules()).thenReturn(rules);
        when(mockResult.isIgnored()).thenReturn(true);

        ObjectNode ignoredNode = new ObjectMapper().createObjectNode();
        walker.handleIgnoredFiles(mockResult, ignoredPath.toString(), ignoredNode);

        // Check results
        assertTrue(ignoredNode.has("ignored/file.txt"), "Ignored file should be listed");
        assertTrue(ignoredNode.path("ignored/file.txt").has("rule"), "Rules should be noted");
    }

    @Test
    @Order(9)
    public void testWriteHumanReadableSummary() throws IOException {
        // Prepare the environment
        Path output = Files.createTempFile("testHumanReadable", ".txt");
        BufferedWriter writer = Files.newBufferedWriter(output);

        // Add data to be written
        walker.includedFiles.addAll(Arrays.asList("file1.txt", "file2.txt"));
        walker.ignoredFiles.addAll(Arrays.asList("ignored1.txt", "ignored2.txt"));

        // Execute
        walker.writeHumanReadableSummary(writer);
        writer.close();

        // Verify content
        List<String> lines = Files.readAllLines(output);
        assertTrue(lines.contains("● Total Files Processed: 4"), "Should list total files processed");
        assertTrue(lines.contains("● [INCLUDED] file1.txt"), "Included file should be listed");

        // Clean up
        Files.delete(output);
    }

    @Test
    @Order(10)
    public void testWriteJsonSummary() throws IOException {
        // Prepare JSON nodes
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode summaryNode = rootNode.putObject("summary");

        // Set up test data
        walker.includedFiles.addAll(Arrays.asList("file1.txt", "file2.txt"));
        walker.ignoredFiles.addAll(Arrays.asList("ignored1.txt", "ignored2.txt"));

        // Prepare the environment
        Path output = Files.createTempFile("testJsonSummary", ".json");
        walker.machineReadablePath = output;  // Set the path used by the walker to our controlled temp file

        // Execute
        walker.writeJsonSummary(rootNode, summaryNode);

        // Verify JSON content
        String jsonContent = new String(Files.readAllBytes(output));
        System.out.println(jsonContent);  // Optional: for debugging to see what's written to the JSON file
        assertTrue(jsonContent.contains("\"totalFilesProcessed\""), "JSON should include total files processed");
        assertTrue(jsonContent.contains("\"filesIncluded\""), "JSON should include count of included files");
        assertTrue(jsonContent.contains("\"filesIgnored\""), "JSON should include count of ignored files");
        assertTrue(jsonContent.contains("\"executionTime\""), "JSON should include execution time");
        assertTrue(jsonContent.contains("\"conflictsDetected\""), "JSON should include count of detected conflicts");

        // Clean up
        Files.delete(output);
    }


}
