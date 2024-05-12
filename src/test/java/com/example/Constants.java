package com.example;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
     public static Path testPath = Paths.get("src/test/resources/fs/");
     public static Path humanReadablePath = Paths.get("src/test/resources/fs/human_readable.txt");
     public static Path machineReadablePath = Paths.get("src/test/resources/fs/machine_readable.json");
     public static Path startPath = Paths.get("src/");
     public static Path gitIgnorePath = Paths.get("src/test/resources/sample.gitignore");
     public static Path doNotIgnoreMePath = Paths.get("do_not_ignore_me.log");
     public static Path fileMatcherPath = Paths.get("FileMatcher.java");
     public static Path testLogPath = Paths.get("test.log");
     public static Path randomFilePath = Paths.get("randomfile.txt");
}
