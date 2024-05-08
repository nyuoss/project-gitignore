package com.example.fs;

import org.junit.Before;
import org.junit.Test;

import static com.example.Constants.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileMatcherTest {

    private FileMatcher fileMatcher;

    @Before
    public void setUp() {
        fileMatcher = new FileMatcher(gitIgnorePath);
    }

    @Test
    public void testIsIgnored() {
        assertTrue("Should ignore .log files", fileMatcher.isIgnored(testLogPath).isIgnored());
        assertFalse("Should not ignore .java files", fileMatcher.isIgnored(fileMatcherPath).isIgnored());
    }

    @Test
    public void testIsIgnoredWithNegation() {
        // Assuming the .gitignore negates ignore for specific files
        assertFalse("Should not ignore specific negated files", fileMatcher.isIgnored(doNotIgnoreMePath).isIgnored());
    }

    @Test
    public void testNoRulesApplied() {
        // This tests the behavior when no rules are matched
        assertFalse("Should not ignore when no applicable rules", fileMatcher.isIgnored(randomFilePath).isIgnored());
    }
}
