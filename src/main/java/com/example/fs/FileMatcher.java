package com.example.fs;

import com.example.rules.Rule;
import com.example.rules.RuleParser;
import com.example.rules.RuleResult;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileMatcher {
    private List<Rule> rules;
    private static final Logger LOGGER = Logger.getLogger(FileMatcher.class.getName());

    public FileMatcher(Path gitignorePath) {
        RuleParser ruleParser = new RuleParser();
        try {
            rules = ruleParser.parseRules(gitignorePath);
            LOGGER.info("Rules parsed successfully from: " + gitignorePath);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse .gitignore file at " + gitignorePath, e);
            rules = List.of(); // Ensure rules is not null
        }
    }

    public RuleResult isIgnored(Path file) {
        String filePath = file.toAbsolutePath().normalize().toString();
        List<Rule> matchingRules = new ArrayList<>();
        boolean ignored = false;

        for (Rule rule : rules) {
            try {
                if (rule.matches(filePath)) {
                    matchingRules.add(rule);
                    ignored = !rule.isNegation();
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to match rule for file: " + filePath, e);
            }
        }

        // Determine the final ignore status based on the last matching rule, if any
        if (!matchingRules.isEmpty()) {
            Rule lastRule = matchingRules.get(matchingRules.size() - 1);
            ignored = !lastRule.isNegation();
        }

        LOGGER.fine("File: " + filePath + " ignored status: " + ignored);
        return new RuleResult(ignored, matchingRules);
    }
}
