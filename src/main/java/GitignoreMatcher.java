import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class GitignoreMatcher {
    private List<Pattern> includePatterns = new ArrayList<>();
    private List<Pattern> excludePatterns = new ArrayList<>();

    public void addPattern(String gitignorePattern) {
        if (gitignorePattern.trim().isEmpty() || gitignorePattern.startsWith("#")) {
            return; // Skip empty lines and comments
        }

        boolean negate = false;
        if (gitignorePattern.startsWith("!")) {
            gitignorePattern = gitignorePattern.substring(1);
            negate = true;
        }

        String regex = convertToRegex(gitignorePattern);
        if (negate) {
            includePatterns.add(Pattern.compile(regex));
        } else {
            excludePatterns.add(Pattern.compile(regex));
        }
        System.out.println("Pattern added: " + regex + (negate ? " (include)" : " (exclude)"));
    }

    private String convertToRegex(String pattern) {
        StringBuilder regex = new StringBuilder();
        regex.append(".*"); // Match from any directory prefix
        pattern = pattern.replace(".", "\\.").replace("?", "[^/]").replace("**/", ".*").replace("**", ".*").replace("*", "[^/]*");
        regex.append(pattern).append("$");
        return regex.toString();
    }

    public boolean isIgnored(Path file) {
        String relativePath = file.toString().replace("\\", "/");  // Normalize path
        System.out.println("Checking file: " + relativePath);

        for (Pattern include : includePatterns) {
            if (include.matcher(relativePath).matches()) {
                System.out.println("Included by pattern: " + include);
                return false;
            }
        }

        for (Pattern exclude : excludePatterns) {
            if (exclude.matcher(relativePath).matches()) {
                System.out.println("Excluded by pattern: " + exclude);
                return true;
            }
        }

        return false;  // Default to not ignored if no patterns match
    }
}
