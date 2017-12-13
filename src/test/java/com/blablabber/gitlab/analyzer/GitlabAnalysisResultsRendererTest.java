package com.blablabber.gitlab.analyzer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class GitlabAnalysisResultsRendererTest {

    private GitlabAnalysisResultsRenderer gitlabAnalysisResultsRenderer;
    private String result;

    @Before
    public void setUp() throws Exception {
        gitlabAnalysisResultsRenderer = new GitlabAnalysisResultsRenderer();
    }

    @Test
    public void shouldReturnStatusQuoWhenNoNewOrOldViolations() throws Exception {
        renderViolations(new ArrayList<>(), new ArrayList<>());
        verifySubstring("Keeping status quo. No violations added or removed.");
    }

    @Test
    public void shouldReturnPositiveMessageWhenViolationsDecrease() throws Exception {
        renderViolations(singletonList("one"), asList("one", "two"));
        verifySubstring("Congratulations! The code now has 1 less violations.");
    }

    @Test
    public void shouldReturnNegativeMessageWhenViolationsIncrease() throws Exception {
        renderViolations(asList("one", "two"), singletonList("one"));
        verifySubstring("Number of added violations: 1.");
    }

    @Test
    public void shouldListAddedViolationsAsList() throws Exception {
        renderViolations(asList("one", "NewlyAddedViolation"), singletonList("one"));
        verifySubstring("- NewlyAddedViolation");
    }

    @Test
    public void shouldListResolvedViolationsAsList() throws Exception {
        renderViolations(singletonList("one"), asList("one", "resolvedViolation"));
        verifyAbsentSubstring("~~one~~");
        verifySubstring("- ~~resolvedViolation~~");
    }

    @Test
    public void allCombinations() throws Exception {
        renderViolations(asList("one", "two", "three"), asList("two", "three", "four"));
        //TODO add context
    }

    private void renderViolations(final List<String> sourceViolations, final List<String> one) {
        result = gitlabAnalysisResultsRenderer.render(new MergeRequestAnalysisResult(null, sourceViolations, one));
    }

    private void verifySubstring(final String expectedSubstring) {
        System.out.println(result);
        assertThat(result, containsString(expectedSubstring));
    }

    private void verifyAbsentSubstring(final String expectedSubstring) {
        System.out.println(result);
        assertFalse(result.contains(expectedSubstring));
    }

}