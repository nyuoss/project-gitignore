package com.example.fs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.example.rules.Rule;
import com.example.rules.RuleResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class FileSystemWalker {
    /**
     * - Traverses all files and directories starting from a specified root path
     * using Java NIO's Files.walk.
     * - Utilizes FileMatcher to check each file and directory against provided
     * .gitignore rules.
     * - Collects and categorizes files into 'included' and 'ignored' based on
     * whether they are affected by .gitignore rules.
     * - Detects conflicts where multiple rules affect the same file and reports
     * these for clarity and debugging.
     * - Writes a comprehensive human-readable summary to an output file, detailing:
     * - Total number of files processed.
     * - Breakdown of files included for tracking and those ignored.
     * - Any rule conflicts detected.
     * - Execution time for the file system traversal.
     * - Writes a comprehensive machine-readable summary to an output file,
     * detailing:
     * - (to be implemented)
     */

    private FileMatcher fileMatcher;
    private Path humanReadablePath;
    private Path machineReadablePath; // Path for JSON output
    private ObjectMapper mapper = new ObjectMapper();

    private List<String> includedFiles = new ArrayList<>();
    private List<String> ignoredFiles = new ArrayList<>();
    private Map<String, List<String>> conflicts = new HashMap<>();
    private long startTime;
    private long endTime;

    public FileSystemWalker(Path gitignorePath, Path humanReadablePath, Path machineReadablePath) {
        this.fileMatcher = new FileMatcher(gitignorePath);
        this.humanReadablePath = humanReadablePath;
        this.machineReadablePath = machineReadablePath;
    }

    public void walkFileTree(Path startPath) {
        startTime = System.currentTimeMillis();
        ObjectNode rootNode = mapper.createObjectNode();
        ObjectNode summaryNode = rootNode.putObject("summary");
        ObjectNode indexesNode = rootNode.putObject("indexes");
        ObjectNode includedNode = indexesNode.putObject("includedFiles");
        ObjectNode ignoredNode = indexesNode.putObject("ignoredFiles");

        try (Stream<Path> paths = Files.walk(startPath);
                BufferedWriter writer = Files.newBufferedWriter(humanReadablePath, StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING)) {
            paths.forEach(path -> {
                RuleResult result = fileMatcher.isIgnored(path);
                String pathString = path.toString();

                if (!result.isIgnored()) {
                    includedFiles.add(pathString);
                    includedNode.putObject(pathString).put("status", "INCLUDED");
                } else {
                    String ruleDetails = result.getMatchingRules().stream()
                            .map(Rule::toString)
                            .collect(Collectors.joining(", "));
                    String formattedStatus = "[EXCLUDED by `" + ruleDetails + "`] ";
                    ignoredFiles.add(formattedStatus + pathString);
                    ignoredNode.putObject(pathString)
                            .put("rule", ruleDetails)
                            .put("status", formattedStatus);

                    if (result.getMatchingRules().size() > 1) {
                        conflicts.put(pathString,
                                result.getMatchingRules().stream().map(Rule::toString).collect(Collectors.toList()));
                    }
                }
            });
            endTime = System.currentTimeMillis();
            writeHumanReadableSummary(writer);
            writeJsonSummary(rootNode, summaryNode);
        } catch (IOException e) {
            System.err.println("Error walking the file system: " + e.getMessage());
        }
    }

    private void writeHumanReadableSummary(BufferedWriter writer) throws IOException {
        writer.write("Gitignore Verifier Summary:\n");
        writer.write("● Total Files Processed: " + (includedFiles.size() + ignoredFiles.size()) + "\n");
        writer.write("● Files Included for Tracking: " + includedFiles.size() + "\n");
        writer.write("● Files Ignored: " + ignoredFiles.size() + "\n");
        writer.write("● Execution Time: " + ((endTime - startTime) / 1000.0) + " seconds\n\n");

        writer.write("Matched Files:\n");
        for (String file : includedFiles) {
            writer.write("● [INCLUDED] " + file + "\n");
        }

        writer.write("\nIgnored Files:\n");
        for (String file : ignoredFiles) {
            writer.write("● " + file + "\n");
        }

        if (!conflicts.isEmpty()) {
            writer.write("\nConflicts Detected:\n");
            conflicts.forEach((file, rules) -> {
                try {
                    writer.write("● " + file + " affected by conflicting rules: " + String.join(", ", rules) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            writer.write("\nNo Conflicts detected\n");
        }
    }

    private void writeJsonSummary(ObjectNode rootNode, ObjectNode summaryNode) throws IOException {
        summaryNode.put("totalFilesProcessed", includedFiles.size() + ignoredFiles.size());
        summaryNode.put("filesIncluded", includedFiles.size());
        summaryNode.put("filesIgnored", ignoredFiles.size());
        summaryNode.put("executionTime", ((endTime - startTime) / 1000.0) + " seconds");
        summaryNode.put("conflictsDetected", conflicts.size());

        try (BufferedWriter jsonWriter = Files.newBufferedWriter(machineReadablePath, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            jsonWriter.write(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
        }
    }
}
