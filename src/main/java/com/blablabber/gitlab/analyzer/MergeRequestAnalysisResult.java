package com.blablabber.gitlab.analyzer;

import com.blablabber.gitlab.api.GitLabMergeRequest;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@JsonPropertyOrder({"message", "gitLabMergeRequest", "newViolations", "sourceViolations", "targetViolations"})
public class MergeRequestAnalysisResult {
    private final String message;
    private GitLabMergeRequest gitLabMergeRequest;
    private final List<String> newViolations;
    private final List<String> resolvedViolations;
    private List<String> sourceViolations;
    private List<String> targetViolations;
    private final List<String> unresolvedViolations;

    public MergeRequestAnalysisResult(GitLabMergeRequest gitLabMergeRequest, List<String> sourceViolations, List<String> targetViolations) {
        this.gitLabMergeRequest = gitLabMergeRequest;
        this.sourceViolations = sourceViolations;
        this.targetViolations = targetViolations;
        this.newViolations = new ArrayList<>(sourceViolations);
        this.newViolations.removeAll(targetViolations);
        this.resolvedViolations = new ArrayList<>(targetViolations);
        resolvedViolations.removeAll(sourceViolations);
        this.unresolvedViolations = new ArrayList<>(targetViolations);
        unresolvedViolations.removeAll(resolvedViolations);
        int addedViolations = sourceViolations.size() - targetViolations.size();
        if (addedViolations > 0) {
            this.message = "Number of added violations: " + addedViolations + ".";
        } else if (addedViolations == 0) {
            this.message = "Keeping status quo. No violations added or removed.";
        } else {
            this.message = "Congratulations! The code now has " + Math.negateExact(addedViolations) + " less violations.";
        }
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

    public List<String> getResolvedViolations() {
        return resolvedViolations;
    }

    public List<String> getUnresolvedViolations() {
        return unresolvedViolations;
    }

    @Override
    public String toString() {
        return message + "\n" +
                ", gitLabMergeRequest=" + gitLabMergeRequest + "\n" +
                ", newViolations=" + newViolations + "\n" +
                ", sourceViolations=" + sourceViolations + "\n" +
                ", targetViolations=" + targetViolations;
    }
}
