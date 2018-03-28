package com.blablabber.analysis.pmd;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class PmdAnalyzerIT {

    private PmdAnalyzer pmdAnalyzer;

    @Before
    public void setUp() throws Exception {
        pmdAnalyzer = new PmdAnalyzer();
    }

    @Test
    public void shouldAnalyzeAndReturnViolations() throws Exception {
        List<String> violations = pmdAnalyzer.analyze(copyResourceToDisk("/analysis/pmd/JavaClassWithViolations.java"));
        assertThat(violations.get(0), containsString("Medium: SomeClass:3 - Do not use the short type"));
    }

    private String copyResourceToDisk(String resource) throws IOException {
        InputStream is = getClass().getResourceAsStream(resource);
        Path target = File.createTempFile("pmd-analysis", null).toPath();
        Files.copy(is, target, REPLACE_EXISTING);
        return target.toString();
    }

}