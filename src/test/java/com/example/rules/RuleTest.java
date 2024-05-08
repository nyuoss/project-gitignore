package com.example.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RuleTest {
    private Rule rule;

    @Test
    public void testSimpleWildcard() {
        rule = new Rule("*.log");
        assertTrue(rule.matches("error.log"));
        assertFalse(rule.matches("error.log/file.txt"));
        //assertFalse(rule.matches("log/error.log"));
    }

    @Test
    public void testNegation() {
        rule = new Rule("!important.log");
        assertTrue(rule.isNegation());
        assertTrue(rule.matches("important.log"));
        assertFalse(rule.matches("not_important.log"));
    }

    @Test
    public void testDirectoryWildcard() {
        rule = new Rule("logs/**/*.log");
        assertTrue(rule.matches("logs/error.log"));
        assertTrue(rule.matches("logs/subdir/error.log"));
        assertFalse(rule.matches("logs/subdir/subsubdir/info.txt"));
    }

    @Test
    public void testRootDirectoryWildcard() {
        rule = new Rule("/build/**/*.o");
        assertTrue(rule.matches("build/test.o"));
        assertTrue(rule.matches("build/subdir/test.o"));
        assertFalse(rule.matches("not_build/test.o"));
        assertFalse(rule.matches("build/test.txt"));
    }

    @Test
    public void testComplexPattern() {
        rule = new Rule("**/temp/*");
        assertTrue(rule.matches("temp/error.tmp"));
        assertTrue(rule.matches("sub/temp/error.tmp"));
        assertFalse(rule.matches("temp"));
    }

    @Test
    public void testFullPath() {
        rule = new Rule("src/main/java/**/*.java");
        assertTrue(rule.matches("src/main/java/com/example/App.java"));
        assertTrue(rule.matches("src/main/java/com/example/utils/Helper.java"));
        assertFalse(rule.matches("src/main/cpp/com/example/App.cpp"));
    }
}
