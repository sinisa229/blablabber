package com.blablabber.gitlab.analyzer;

import com.blablabber.analysis.pmd.PmdAnalyzer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.blablabber.file.TestPathUtil.getMockPath;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class GitlabAnalyzerTest {

    @Mock
    private MergeRequestFileCollector mergeRequestFileCollector;

    @Mock
    private PmdAnalyzer pmdAnalyzer;

    @InjectMocks
    private GitlabAnalyzer gitlabAnalyzer;

    @Test
    public void name() throws Exception {
        doReturn(getMockPath("sourceDir")).when(mergeRequestFileCollector).getSourceDirectory();
        doReturn(getMockPath("targetDir")).when(mergeRequestFileCollector).getTargetDirectory();

        doReturn(asList("sourceViolation")).when(pmdAnalyzer).analyze("sourceDir");
        doReturn(asList("targetViolation")).when(pmdAnalyzer).analyze("targetDir");

        MergeRequestAnalysisResult result = gitlabAnalyzer.analyze(mergeRequestFileCollector);

        assertThat(result.getSourceViolations().get(0), equalTo("sourceViolation"));
        assertThat(result.getTargetViolations().get(0), equalTo("targetViolation"));
    }
}