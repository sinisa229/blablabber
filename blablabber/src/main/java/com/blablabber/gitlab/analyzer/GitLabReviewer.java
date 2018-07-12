package com.blablabber.gitlab.analyzer;

import com.blablabber.gitlab.api.model.GitLabInfo;
import com.blablabber.gitlab.api.GitlabApiClient;
import com.blablabber.gitlab.api.model.MergeRequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitLabReviewer {

    private final GitlabApiClient gitlabApiClient;

    private final GitlabAnalyzer pmdAnalyzer;
    private final GitlabAnalysisResultsRenderer gitlabAnalysisResultsRenderer;

    @Autowired
    public GitLabReviewer(GitlabApiClient gitlabApiClient, GitlabAnalyzer pmdAnalyzer, GitlabAnalysisResultsRenderer gitlabAnalysisResultsRenderer) {
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
        mergeRequestFileCollector.fetchFiles(gitlabApiClient.getMyGitLabMergeRequests(gitLabInfo), o -> analyzeAndComment(gitLabInfo, mergeRequestAnalysisResults, o));
        return mergeRequestAnalysisResults;
    }

    private void analyzeAndComment(final GitLabInfo gitLabInfo, final List<MergeRequestAnalysisResult> mergeRequestAnalysisResults, final MergeRequestFileCollector o) {
        final String render = gitlabAnalysisResultsRenderer.render(analyze(mergeRequestAnalysisResults, o));
        gitlabApiClient.postMergeRequestComment(gitLabInfo, o.getGitLabMergeRequest(), render);
    }

    public List<MergeRequestAnalysisResult> analysisPreview(final GitLabInfo gitLabInfo, final MergeRequestInfo mergeRequestInfo) {
        List<MergeRequestAnalysisResult> mergeRequestAnalysisResults = new ArrayList<>();
        new MergeRequestFileCollector(gitlabApiClient, gitLabInfo)
                .fetchFiles(
                        gitlabApiClient.getMergeRequest(gitLabInfo, mergeRequestInfo),
                        o -> analyze(mergeRequestAnalysisResults, o));
        return mergeRequestAnalysisResults;
    }

    private MergeRequestAnalysisResult analyze(final List<MergeRequestAnalysisResult> mergeRequestAnalysisResults, final MergeRequestFileCollector o) {
        MergeRequestAnalysisResult mergeRequestAnalysisResult = pmdAnalyzer.analyze(o);
        mergeRequestAnalysisResults.add(mergeRequestAnalysisResult);
        return mergeRequestAnalysisResult;
    }
}
