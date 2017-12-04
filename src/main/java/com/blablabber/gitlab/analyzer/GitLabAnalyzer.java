package com.blablabber.gitlab.analyzer;

import com.blablabber.analysis.pmd.PmdAnalyzer;
import com.blablabber.gitlab.api.GitlabMergeRequestProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitLabAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitLabAnalyzer.class);

    private GitlabMergeRequestProvider gitlabMergeRequestProvider;

    private PmdAnalyzer pmdAnalyzer;

    @Autowired
    public GitLabAnalyzer(GitlabMergeRequestProvider gitlabMergeRequestProvider, PmdAnalyzer pmdAnalyzer) {
        this.gitlabMergeRequestProvider = gitlabMergeRequestProvider;
        this.pmdAnalyzer = pmdAnalyzer;
    }

    public void startAnalysis(String repositoryBaseUrl) {
        final MergeRequestFileCollector mergeRequestFileCollector = new MergeRequestFileCollector(gitlabMergeRequestProvider, repositoryBaseUrl);
        mergeRequestFileCollector.fetchFiles();
        List<String> sourceViolations = pmdAnalyzer.analyze(mergeRequestFileCollector.getSourceDirectory().toString());
        List<String> targetViolations = pmdAnalyzer.analyze(mergeRequestFileCollector.getTargetDirectory().toString());
        System.out.println("-------------------------------------------------------------");
        System.out.println("Number of new violations: " + sourceViolations.size() + " number of old violations: " + targetViolations.size());
        LOGGER.info("Violations in new code that are not present in old code:");
        final ArrayList<String> sourceCopy = new ArrayList<>(sourceViolations);
        sourceCopy.removeAll(targetViolations);
        LOGGER.info("{}", sourceCopy);
        System.out.println("-------------------------------------------------------------");
        LOGGER.info("Violations in old code that are not present in new code:");
        final ArrayList<String> targetCopy = new ArrayList<>(targetViolations);
        final boolean oldViolations = targetCopy.removeAll(sourceViolations);
        LOGGER.info("{}", oldViolations);
    }

}
