package com.blablabber.gitlab.analyzer;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class GitlabAnalysisResultsRendererTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabAnalysisResultsRendererTest.class);

    private GitlabAnalysisResultsRenderer gitlabAnalysisResultsRenderer;
    private String result;

    @Before
    public void setUp() throws Exception {
        gitlabAnalysisResultsRenderer = new GitlabAnalysisResultsRenderer();
    }

    @Test
    public void shouldReturnStatusQuoWhenNoNewOrOldViolations() throws Exception {
        renderViolations(new ArrayList<>(), new ArrayList<>());
        verifySubstring("Keeping status quo. The number of total violations remains unchanged.");
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
    public void oneNewAddedOneOldRemoved() throws Exception {
        renderViolations(asList("one", "two", "three"), asList("two", "three", "four"));
        verifySubstring("Keeping status quo. The number of total violations remains unchanged");
        verifySubstring("## New violations\n\n- one");
        verifySubstring("## Unresolved violations\n\n- two\n- three");
        verifySubstring("## Resolved violations\n\n- ~~four~~");
    }

    private void renderViolations(final List<String> sourceViolations, final List<String> one) {
        result = gitlabAnalysisResultsRenderer.render(new MergeRequestAnalysisResult(null, sourceViolations, one));
        LOGGER.info(result);
    }

    private void verifySubstring(final String expectedSubstring) {
        assertThat(result, containsString(expectedSubstring));
    }

    private void verifyAbsentSubstring(final String expectedSubstring) {
        assertFalse(result.contains(expectedSubstring));
    }

}