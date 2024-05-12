package com.example.fs;

import com.example.rules.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.example.Constants.gitIgnorePath;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileMatcherTest {

    private FileMatcher fileMatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Rule> mockRules = Arrays.asList(new Rule("*.tmp"), new Rule("*.log"), new Rule("!important.log"));
        fileMatcher = new FileMatcher(gitIgnorePath);

        setInternalState(fileMatcher, "rules", mockRules);
    }

    @Test
    public void testIgnoreTmpFiles() {
        assertTrue(fileMatcher.isIgnored(Paths.get("file.tmp")).isIgnored(), "Temporary files should be ignored.");
    }

    @Test
    public void testWildcardPatternExclusions() {
        assertTrue(fileMatcher.isIgnored(Paths.get("test.log")).isIgnored(), "Log files should be ignored due to wildcard pattern.");
        assertFalse(fileMatcher.isIgnored(Paths.get("important.log")).isIgnored(), "Important log file should not be ignored due to negation rule.");
    }

    @Test
    public void testNegationRulePriority() {
        assertFalse(fileMatcher.isIgnored(Paths.get("important.log")).isIgnored(), "Important.log should not be ignored despite the general log ignore rule.");
    }

    public static void setInternalState(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set internal state due to: " + e.getMessage(), e);
        }
    }

}