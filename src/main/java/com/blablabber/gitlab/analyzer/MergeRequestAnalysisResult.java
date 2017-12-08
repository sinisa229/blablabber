package com.blablabber.gitlab.analyzer;

import com.blablabber.gitlab.api.GitLabMergeRequest;

import java.util.ArrayList;
import java.util.List;

public class MergeRequestAnalysisResult {
    private String message;
    private GitLabMergeRequest gitLabMergeRequest;
    private List<String> newViolations;
    private List<String> sourceViolations;
    private List<String> targetViolations;

    public MergeRequestAnalysisResult(GitLabMergeRequest gitLabMergeRequest, List<String> sourceViolations, List<String> targetViolations) {
        this.gitLabMergeRequest = gitLabMergeRequest;
        this.sourceViolations = sourceViolations;
        this.targetViolations = targetViolations;
        this.newViolations = new ArrayList<>(sourceViolations);
        this.newViolations.removeAll(targetViolations);
        this.message = "Number of added violations: " + (sourceViolations.size() - targetViolations.size()) + ".";
    }

    public GitLabMergeRequest getGitLabMergeRequest() {
        return gitLabMergeRequest;
    }

    public void setGitLabMergeRequest(GitLabMergeRequest gitLabMergeRequest) {
        this.gitLabMergeRequest = gitLabMergeRequest;
    }

    public List<String> getSourceViolations() {
        return sourceViolations;
    }

    public void setSourceViolations(List<String> sourceViolations) {
        this.sourceViolations = sourceViolations;
    }

    public List<String> getTargetViolations() {
        return targetViolations;
    }

    public void setTargetViolations(List<String> targetViolations) {
        this.targetViolations = targetViolations;
    }

    public List<String> getNewViolations() {
        return newViolations;
    }

    public String getMessage() {
        return message;
    }

}
