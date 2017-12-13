package com.blablabber.gitlab.analyzer;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
class GitlabAnalysisResultsRenderer {

    public String render(final MergeRequestAnalysisResult mergeRequestAnalysisResult) {
        StringBuilder comment = new StringBuilder();
        final String message = mergeRequestAnalysisResult.getMessage();
        if (message != null) {
            addTitle(comment, message);
        }
        renderNewViolations(mergeRequestAnalysisResult, comment);
        renderUnresolvedViolations(mergeRequestAnalysisResult, comment);
        renderResolvedViolations(mergeRequestAnalysisResult, comment);
        return comment.toString();
    }

    private void renderNewViolations(final MergeRequestAnalysisResult mergeRequestAnalysisResult, final StringBuilder comment) {
        final List<String> newViolations = mergeRequestAnalysisResult.getNewViolations();
        renderTitleForList(comment, newViolations, "No new violations", "New violations");
        newViolations.forEach(s -> addAsListItem(comment, s));
    }

    private void renderUnresolvedViolations(final MergeRequestAnalysisResult mergeRequestAnalysisResult, final StringBuilder comment) {
        final List<String> unresolvedViolations = mergeRequestAnalysisResult.getUnresolvedViolations();
        renderTitleForList(comment, unresolvedViolations, "All existing violations resolved", "Unresolved violations");
        unresolvedViolations.forEach(s -> addAsListItem(comment, s));
    }

    private void renderResolvedViolations(final MergeRequestAnalysisResult mergeRequestAnalysisResult, final StringBuilder comment) {
        final List<String> resolvedViolations = mergeRequestAnalysisResult.getResolvedViolations();
        renderTitleForList(comment, resolvedViolations, "No resolved violations", "Resolved violations");
        resolvedViolations.forEach(s -> addAsStrikethroughListItem(comment, s));
    }

    private void renderTitleForList(final StringBuilder comment, final List<String> violations, final String no_new_violations, final String new_violations) {
        if (violations.isEmpty()) {
            addTitle(comment, no_new_violations);
        } else {
            addTitle(comment, new_violations);
        }
    }

    private void addTitle(final StringBuilder comment, String title) {
        comment.append(getTitle(title)).append("\n");
    }

    private String getTitle(final String title) {
        return "## " + title+"\n";
    }

    private void addAsStrikethroughListItem(final StringBuilder comment, final String s) {
        addAsListItem(comment, strikethrough(s));
    }

    private String strikethrough(final String s) {
        return "~~" + s + "~~";
    }

    private void addGreenItem(final StringBuilder comment, final String s) {
        addAsListItem(comment, makeGreen(s));
    }

    private void addAsListItem(final StringBuilder comment, final String str) {
        comment.append("- ").append(str).append("\n");
    }

    private String makeGreen(final String s) {
        return "{+ " + s + " +}";
    }
}
