package com.blablabber.gitlab.analyzer;

import com.blablabber.analysis.Analyzer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.blablabber.file.TestPathUtil.getMockPath;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class GitlabAnalyzerTest {

    @Mock
    private MergeRequestFileCollector mergeRequestFileCollector;

    @Mock
    private Analyzer analyzer;

    private GitlabAnalyzer gitlabAnalyzer;

    @Before
    public void setUp() {
        doReturn(getMockPath("sourceDir")).when(mergeRequestFileCollector).getSourceDirectory();
        doReturn(getMockPath("targetDir")).when(mergeRequestFileCollector).getTargetDirectory();

        doReturn(singletonList("sourceViolation")).when(analyzer).analyze("sourceDir");
        doReturn(singletonList("targetViolation")).when(analyzer).analyze("targetDir");
    }

    @Test
    public void shouldReturnAllViolationsForTargetAndSource() throws Exception {
        gitlabAnalyzer = new GitlabAnalyzer(singletonList(analyzer));
        MergeRequestAnalysisResult result = gitlabAnalyzer.analyze(mergeRequestFileCollector);
        assertThat(result.getSourceViolations().get(0), equalTo("sourceViolation"));
        assertThat(result.getTargetViolations().get(0), equalTo("targetViolation"));
    }

    @Test
    public void shouldReturnAllCombinedViolationsForTargetAndSource() throws Exception {
        gitlabAnalyzer = new GitlabAnalyzer(asList(analyzer, analyzer));
        MergeRequestAnalysisResult result = gitlabAnalyzer.analyze(mergeRequestFileCollector);
        assertThat(result.getSourceViolations().get(0), equalTo("sourceViolation"));
        assertThat(result.getTargetViolations().get(0), equalTo("targetViolation"));
        assertThat(result.getSourceViolations().get(1), equalTo("sourceViolation"));
        assertThat(result.getTargetViolations().get(1), equalTo("targetViolation"));
    }

}