import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MainAttempt {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java HelloWorld <directory_path>");
            System.exit(1);
        }

        Path startPath = Paths.get(args[0]);

        GitignoreMatcher matcher = new GitignoreMatcher();
        matcher.addPattern("*.log");  // Modify or add patterns as needed
        matcher.addPattern("!important.log");
        matcher.addPattern("temp/");
        matcher.addPattern("*.txt");
        matcher.addPattern("test/*.tmp");

        try (Stream<Path> paths = Files.walk(startPath)) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    boolean isIgnored = matcher.isIgnored(filePath);
                    System.out.println("File '" + filePath + "' is ignored: " + isIgnored);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
