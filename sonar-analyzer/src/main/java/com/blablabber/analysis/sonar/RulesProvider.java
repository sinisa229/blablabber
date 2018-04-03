package com.blablabber.analysis.sonar;

import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.java.checks.CheckList;
import org.sonar.plugins.java.JavaRulesDefinition;
import org.sonar.plugins.java.api.JavaFileScanner;

import java.util.List;

import static java.util.Arrays.asList;

public class RulesProvider {

    public List<JavaFileScanner> getRules(List<String> ruleIds) {
        JavaRulesDefinition definition = new JavaRulesDefinition(new MapSettings().asConfig());
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        RulesDefinition.Repository repository = context.repository("squid");
        final RulesDefinition.Rule s2095 = repository.rule("S2095");
        //TODO continue here
        return null;
    }

}
