package com.blablabber.gitlab.analyzer;

import com.blablabber.gitlab.api.GitLabInfo;
import com.blablabber.gitlab.api.GitlabApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitLabReviewer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitLabReviewer.class);

    private final GitlabApiClient gitlabApiClient;

    private final GitlabAnalyzer pmdAnalyzer;
    private final GitlabAnalysisResultsRenderer gitlabAnalysisResultsRenderer;

    @Autowired
    private GitLabReviewer(GitlabApiClient gitlabApiClient, GitlabAnalyzer pmdAnalyzer, GitlabAnalysisResultsRenderer gitlabAnalysisResultsRenderer) {
        this.gitlabApiClient = gitlabApiClient;
        this.pmdAnalyzer = pmdAnalyzer;
        this.gitlabAnalysisResultsRenderer = gitlabAnalysisResultsRenderer;
    }

    public List<MergeRequestAnalysisResult> analysisPreview(GitLabInfo gitLabInfo) {
        final MergeRequestFileCollector mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, gitLabInfo);
        List<MergeRequestAnalysisResult> mergeRequestAnalysisResults = new ArrayList<>();
        mergeRequestFileCollector.fetchFiles(o -> mergeRequestAnalysisResults.add(pmdAnalyzer.analyze(o)));
        return mergeRequestAnalysisResults;
    }

    public List<MergeRequestAnalysisResult> codeReview(GitLabInfo gitLabInfo) {
        final MergeRequestFileCollector mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, gitLabInfo);
        List<MergeRequestAnalysisResult> mergeRequestAnalysisResults = new ArrayList<>();
        mergeRequestFileCollector.fetchFiles(gitlabApiClient.getMyGitLabMergeRequests(gitLabInfo), o -> {
            MergeRequestAnalysisResult mergeRequestAnalysisResult = pmdAnalyzer.analyze(o);
            mergeRequestAnalysisResults.add(mergeRequestAnalysisResult);
            final String render = gitlabAnalysisResultsRenderer.render(mergeRequestAnalysisResult);
            gitlabApiClient.postMergeRequestComment(gitLabInfo, o.getGitLabMergeRequest(), render);
        });
        return mergeRequestAnalysisResults;
    }

}
