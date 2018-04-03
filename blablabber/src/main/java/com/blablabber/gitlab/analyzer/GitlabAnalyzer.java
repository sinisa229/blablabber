package com.blablabber.gitlab.analyzer;

import com.blablabber.analysis.Analyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
class GitlabAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabAnalyzer.class);
    private final List<Analyzer> analyzers;

    @Autowired
    public GitlabAnalyzer(List<Analyzer> analyzer) {
        this.analyzers = analyzer;
    }

    MergeRequestAnalysisResult analyze(MergeRequestFileCollector mergeRequestFileCollector) {
        LOGGER.info("Analyzing merge request {}", mergeRequestFileCollector.getGitLabMergeRequest());
        List<String> sourceViolations = analyzeSourceDirectory(mergeRequestFileCollector);
        List<String> targetViolations = analyzeTargetDirectory(mergeRequestFileCollector);
        LOGGER.info("Analyzing merge request {} finished", mergeRequestFileCollector.getGitLabMergeRequest());
        return new MergeRequestAnalysisResult(mergeRequestFileCollector.getGitLabMergeRequest(), sourceViolations, targetViolations);
    }

    private List<String> analyzeSourceDirectory(final MergeRequestFileCollector mergeRequestFileCollector) {
        return analyze(mergeRequestFileCollector.getSourceDirectory().toString());
    }

    private List<String> analyzeTargetDirectory(final MergeRequestFileCollector mergeRequestFileCollector) {
        return analyze(mergeRequestFileCollector.getTargetDirectory().toString());
    }

    private List<String> analyze(final String dir) {
        return analyzers.stream().map(analyzer -> analyzer.analyze(dir)).flatMap(Collection::stream).collect(toList());
    }

}
