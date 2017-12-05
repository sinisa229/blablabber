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
public class GitLabAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitLabAnalyzer.class);

    private GitlabApiClient gitlabApiClient;

    private PmdAnalyzer pmdAnalyzer;

    @Autowired
    public GitLabAnalyzer(GitlabApiClient gitlabApiClient, PmdAnalyzer pmdAnalyzer) {
        this.gitlabApiClient = gitlabApiClient;
        this.pmdAnalyzer = pmdAnalyzer;
    }

    public void startAnalysis(GitLabInfo gitLabInfo) {
        final MergeRequestFileCollector mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, gitLabInfo);
        mergeRequestFileCollector.fetchFiles();
        List<String> sourceViolations = pmdAnalyzer.analyze(mergeRequestFileCollector.getSourceDirectory().toString());
        List<String> targetViolations = pmdAnalyzer.analyze(mergeRequestFileCollector.getTargetDirectory().toString());
        printAnalysis(sourceViolations, targetViolations);
    }

    private void printAnalysis(List<String> sourceViolations, List<String> targetViolations) {
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
