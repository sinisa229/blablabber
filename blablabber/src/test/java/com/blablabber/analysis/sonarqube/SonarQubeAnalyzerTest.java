package com.blablabber.analysis.sonarqube;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.blablabber.file.TestPathUtil.copyResourceToDisk;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class SonarQubeAnalyzerTest {

    private SonarQubeAnalyzer sonarQubeAnalyzer;

    @Before
    public void setUp() throws Exception {
        sonarQubeAnalyzer = new SonarQubeAnalyzer();
    }

    @Test
    public void shouldAnalyzeAndReturnViolations() throws Exception {
        List<String> violations = sonarQubeAnalyzer.analyze(copyResourceToDisk("/analysis/sonarqube/NpeViolations.java"));
        assertThat(violations.get(0), containsString("'Avoid catching NullPointerException.' in "));
    }

    @Test
    public void shouldThrowExceptionOnNonParseableClass() throws Exception {
        List<String> violations = sonarQubeAnalyzer.analyze(copyResourceToDisk("/analysis/sonarqube/NonParseableClass.java"));
        assertThat(violations.get(0), containsString("'Cannot parse file' in "));
    }

}