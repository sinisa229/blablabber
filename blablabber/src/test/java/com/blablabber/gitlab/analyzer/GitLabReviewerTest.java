package com.blablabber.gitlab.analyzer;

import com.blablabber.gitlab.api.model.GitLabInfo;
import com.blablabber.gitlab.api.GitlabApiClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GitLabReviewerTest {

    @Mock
    private GitlabApiClient client;
    @Mock
    private GitlabAnalyzer analyzer;
    @Mock
    private GitlabAnalysisResultsRenderer renderer;
    private GitLabInfo gitLabInfo;

    @InjectMocks
    private GitLabReviewer gitLabReviewer;

    @Test
    public void name() throws Exception {
        gitLabInfo = new GitLabInfo("someUrl");
        List<MergeRequestAnalysisResult> results = gitLabReviewer.analysisPreview(gitLabInfo);
    }
}