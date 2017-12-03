package com.blablabber.gitlab.analyzer;

import com.blablabber.analysis.pmd.PmdAnalyzer;
import com.blablabber.gitlab.api.GitlabMergeRequestProvider;
import org.springframework.stereotype.Component;

@Component
public class GitLabAnalyzer {

    private GitlabMergeRequestProvider gitlabMergeRequestProvider;

    private PmdAnalyzer pmdAnalyzer;

    public GitLabAnalyzer(GitlabMergeRequestProvider gitlabMergeRequestProvider, PmdAnalyzer pmdAnalyzer) {
        this.gitlabMergeRequestProvider = gitlabMergeRequestProvider;
        this.pmdAnalyzer = pmdAnalyzer;
    }

    public void startAnalysis(String repositoryBaseUrl) {
        new MergeRequestFileCollector(gitlabMergeRequestProvider, repositoryBaseUrl).fetchFiles();
    }

}
