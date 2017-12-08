package com.blablabber.gitlab.analyzer;

import com.blablabber.analysis.pmd.PmdAnalyzer;
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

    private GitlabApiClient gitlabApiClient;

    private PmdAnalyzer pmdAnalyzer;

    @Autowired
    public GitLabReviewer(GitlabApiClient gitlabApiClient, PmdAnalyzer pmdAnalyzer) {
        this.gitlabApiClient = gitlabApiClient;
        this.pmdAnalyzer = pmdAnalyzer;
    }

    public List<MergeRequestAnalysisResult> analysisPreview(GitLabInfo gitLabInfo) {
        final MergeRequestFileCollector mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, gitLabInfo);
        List<MergeRequestAnalysisResult> mergeRequestAnalysisResults = new ArrayList<>();
        mergeRequestFileCollector.fetchFiles(o -> {
            mergeRequestAnalysisResults.add(mergeRequestProcessed(o));
        });
        return mergeRequestAnalysisResults;
    }

    public List<MergeRequestAnalysisResult> codeReview(GitLabInfo gitLabInfo) {
        final MergeRequestFileCollector mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, gitLabInfo);
        List<MergeRequestAnalysisResult> mergeRequestAnalysisResults = new ArrayList<>();
        mergeRequestFileCollector.fetchFiles(gitlabApiClient.getAllOpenGitLabMergeRequests(gitLabInfo), o -> {
            mergeRequestAnalysisResults.add(mergeRequestProcessed(o));
        });
        return mergeRequestAnalysisResults;
    }

    public MergeRequestAnalysisResult mergeRequestProcessed(MergeRequestFileCollector mergeRequestFileCollector) {
        LOGGER.info("Analyzing merge request {}", mergeRequestFileCollector.getGitLabMergeRequest());
        List<String> sourceViolations = pmdAnalyzer.analyze(mergeRequestFileCollector.getSourceDirectory().toString());
        List<String> targetViolations = pmdAnalyzer.analyze(mergeRequestFileCollector.getTargetDirectory().toString());
        LOGGER.info("Analyzing merge request {} finished", mergeRequestFileCollector.getGitLabMergeRequest());
        return new MergeRequestAnalysisResult(mergeRequestFileCollector.getGitLabMergeRequest(), sourceViolations, targetViolations);
    }
}
