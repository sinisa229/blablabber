package com.blablabber.analysis.sonar;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.sonar.check.Rule;
import org.sonar.java.RspecKey;
import org.sonar.plugins.java.api.JavaFileScanner;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class RulesProvider {

    private Set<Class<?>> rules;

    public RulesProvider() {
        rules = new Reflections("org.sonar.java").getTypesAnnotatedWith(Rule.class);
    }

    public List<JavaFileScanner> getRules(List<String> ruleIds) {
        final List<JavaFileScanner> collect = rules.stream()
                .filter(aClass -> filterAnnotated(aClass, ruleIds))
                .map(this::transformToInstance)
                .collect(toList());
        return collect;
    }

    @SneakyThrows
    private JavaFileScanner transformToInstance(final Class<?> aClass) {
//        aClass.isAssignableFrom(aClass)
        return (JavaFileScanner) aClass.newInstance();
    }

    private boolean filterAnnotated(final Class<?> equals, final List<String> ruleIds) {
        final Rule[] ruleAnnotation = equals.getAnnotationsByType(Rule.class);
        final RspecKey[] rSpecKeyAnnotation = equals.getAnnotationsByType(RspecKey.class);
        final String key = ruleAnnotation[0].key();
        final String rSpecKey = rSpecKeyAnnotation.length > 0 ? rSpecKeyAnnotation[0].value() : null;
        return ruleIds.contains(key) || ruleIds.contains(rSpecKey);
    }

}
