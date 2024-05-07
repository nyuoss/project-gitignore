package com.example.rules;

import java.util.regex.Pattern;

public class Rule {
    private String originalPattern;
    private Pattern regexPattern;
    private boolean isNegation;

    public Rule(String pattern) {
        this.originalPattern = pattern;
        this.isNegation = pattern.startsWith("!");
        String processedPattern = processPattern(isNegation ? pattern.substring(1) : pattern);
        this.regexPattern = Pattern.compile(processedPattern);
    }

    /**
     * Converts a .gitignore pattern into a Java regex pattern.
     * - Handles wildcards '*' and '?'.
     * - Treats directories specifically if the pattern ends with '/'.
     * - Escapes other regex special characters.
     */

    private String processPattern(String pattern) {
        StringBuilder regex = new StringBuilder();
        boolean isRooted = pattern.startsWith("/");
        if (isRooted) {
            // Remove the leading slash and anchor the regex to the start of the line for
            // root-level patterns.
            pattern = pattern.substring(1);
            regex.append("^");
        } else {
            // For non-rooted patterns, ensure they apply only at the directory level.
            regex.append("^(?:.*/)?"); // Allow matching from the root or any subdirectory
        }

        boolean inDirectoryPattern = false; // Track if we are inside a directory-specific pattern

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case '*':
                    if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '*') {
                        i++; // Skip the next '*'
                        regex.append(".*"); // "**" matches across directories
                        if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '/') {
                            i++; // Skip the '/'
                            regex.append("/?"); // Match an optional following slash
                            inDirectoryPattern = true;
                        }
                    } else {
                        if (!inDirectoryPattern) {
                            regex.append("[^/]*"); // Ensure '*' does not cross directory boundaries
                        } else {
                            regex.append(".*");
                        }
                    }
                    break;
                case '?':
                    regex.append("[^/]"); // '?' matches any single character except '/'
                    break;
                case '.':
                case '(':
                case ')':
                case '+':
                case '|':
                case '^':
                case '$':
                case '@':
                case '%':
                case '[':
                case ']':
                case '{':
                case '}':
                    regex.append("\\").append(c); // Escapes special regex characters
                    break;
                case '/':
                    regex.append("/");
                    inDirectoryPattern = true; // Now we are in a directory-specific part of the pattern
                    break;
                default:
                    regex.append(c); // Adds non-special characters directly
            }
        }
        regex.append("$");

        return regex.toString();
    }

    public boolean matches(String filePath) {
        return regexPattern.matcher(filePath).matches();
    }

    public boolean isNegation() {
        return isNegation;
    }

    public String getOriginalPattern() {
        return originalPattern;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "pattern='" + originalPattern + '\'' +
                ", isNegation=" + isNegation +
                '}';
    }
}
