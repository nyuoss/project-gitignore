package com.example.rules;

import java.util.List;

public class RuleResult {
    private boolean isIgnored;
    private List<Rule> matchingRules;

    public RuleResult(boolean isIgnored, List<Rule> matchingRules) {
        this.isIgnored = isIgnored;
        this.matchingRules = matchingRules;
    }

    public boolean isIgnored() {
        return isIgnored;
    }

    public List<Rule> getMatchingRules() {
        return matchingRules;
    }
}
