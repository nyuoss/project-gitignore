package com.example.fs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.example.rules.Rule;
import com.example.rules.RuleParser;
import com.example.rules.RuleResult;

public class FileMatcher {
    private List<Rule> rules;

    public FileMatcher(Path gitignorePath) {
        RuleParser ruleParser = new RuleParser();
        try {
            rules = ruleParser.parseRules(gitignorePath);
        } catch (Exception e) {
            System.err.println("Failed to parse .gitignore file: " + e.getMessage());
            rules = List.of(); // Ensure rules is not null
        }
    }

public RuleResult isIgnored(Path file) {
        String filePath = file.toAbsolutePath().normalize().toString();
        List<Rule> matchingRules = new ArrayList<>();
        boolean ignored = false;

        for (Rule rule : rules) {
            if (rule.matches(filePath)) {
                matchingRules.add(rule);
                ignored = !rule.isNegation();
            }
        }

        // Determine the final ignore status based on the last matching rule, if any
        if (!matchingRules.isEmpty()) {
            Rule lastRule = matchingRules.get(matchingRules.size() - 1);
            ignored = !lastRule.isNegation();
        }

        return new RuleResult(ignored, matchingRules);
    }
}
