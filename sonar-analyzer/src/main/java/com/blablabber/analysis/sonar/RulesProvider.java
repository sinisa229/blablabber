package com.blablabber.analysis.sonar;

import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.java.checks.AssignmentInSubExpressionCheck;
import org.sonar.java.checks.CatchExceptionCheck;
import org.sonar.java.checks.CatchUsesExceptionWithContextCheck;
import org.sonar.java.checks.ClassComplexityCheck;
import org.sonar.java.checks.ClassVariableVisibilityCheck;
import org.sonar.java.checks.CommentedOutCodeLineCheck;
import org.sonar.java.checks.DiamondOperatorCheck;
import org.sonar.java.checks.EmptyFileCheck;
import org.sonar.java.checks.EmptyStatementUsageCheck;
import org.sonar.java.checks.ForLoopCounterChangedCheck;
import org.sonar.java.checks.OperatorPrecedenceCheck;
import org.sonar.java.checks.ReturnEmptyArrayNotNullCheck;
import org.sonar.java.checks.StaticFieldInitializationCheck;
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
        return asList(new AssignmentInSubExpressionCheck(), new AssignmentInSubExpressionCheck(),
                new ClassComplexityCheck(),
                new ClassVariableVisibilityCheck(),
                new CommentedOutCodeLineCheck(),
                new EmptyFileCheck(),
                new EmptyStatementUsageCheck(),
                new ForLoopCounterChangedCheck(),
                new DiamondOperatorCheck(),
                new StaticFieldInitializationCheck(),
                new CatchUsesExceptionWithContextCheck(),
                new CatchExceptionCheck(),
                new OperatorPrecedenceCheck(),
                new ReturnEmptyArrayNotNullCheck()
        );
    }

}
