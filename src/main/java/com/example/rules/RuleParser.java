package com.example.rules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RuleParser {
    
    /**
     * Parses a .gitignore file and returns a list of Rule objects.
     * Handles exceptions internally and returns an empty list in case of errors.
     *
     * @param gitignoreFilePath Path to the .gitignore file
     * @return List of Rule objects
     */

    public List<Rule> parseRules(Path gitignoreFilePath) {
        List<Rule> rules = new ArrayList<>();
        try {
            rules = Files.lines(gitignoreFilePath)
                    .map(String::trim) // Trim each line
                    .filter(line -> !line.isEmpty() && !line.startsWith("#")) // Ignore empty lines and comments
                    .map(Rule::new) // Convert each line into a Rule object
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error reading .gitignore file: " + e.getMessage());
        }
        return rules;
    }
}
