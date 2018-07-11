package com.blablabber.analysis.sonar;

import org.junit.Test;
import org.sonar.java.checks.ClassComplexityCheck;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class RulesProviderTest {

    @Test
    public void shouldFindByRSpecKey() throws Exception {
        assertThat(new RulesProvider().getRules(singletonList("S1311")).get(0)).isInstanceOf(ClassComplexityCheck.class);
    }

    @Test
    public void shouldFindByRuleKey() throws Exception {
        assertThat(new RulesProvider().getRules(singletonList("ClassCyclomaticComplexity")).get(0)).isInstanceOf(ClassComplexityCheck.class);
    }
}