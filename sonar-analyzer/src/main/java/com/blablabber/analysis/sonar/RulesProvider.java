package com.blablabber.analysis.sonar;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.check.Rule;
import org.sonar.java.RspecKey;
import org.sonar.plugins.java.api.JavaFileScanner;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class RulesProvider {

    private static final Logger LOG = LoggerFactory.getLogger(RulesProvider.class);
    private Set<Class<?>> rules;

    public RulesProvider() {
        rules = new Reflections("org.sonar.java").getTypesAnnotatedWith(Rule.class);
    }

    public List<JavaFileScanner> getRules(List<String> ruleIds) {
        final List<JavaFileScanner> collect = rules.stream()
                .filter(aClass -> filterAnnotated(aClass, ruleIds))
                .map(this::transformToInstance)
                .filter(Objects::nonNull)
                .collect(toList());
        return collect;
    }

    @SneakyThrows
    private JavaFileScanner transformToInstance(final Class<?> aClass) {
        if (JavaFileScanner.class.isAssignableFrom(aClass)) {
            return (JavaFileScanner) aClass.newInstance();
        }
        LOG.info("JavaFileScanner not assignable from {}.", aClass);
        return null;
    }

    private boolean filterAnnotated(final Class<?> equals, final List<String> ruleIds) {
        final Rule[] ruleAnnotation = equals.getAnnotationsByType(Rule.class);
        final RspecKey[] rSpecKeyAnnotation = equals.getAnnotationsByType(RspecKey.class);
        final String key = ruleAnnotation[0].key();
        final String rSpecKey = rSpecKeyAnnotation.length > 0 ? rSpecKeyAnnotation[0].value() : null;
        return ruleIds.contains(key) || ruleIds.contains(rSpecKey);
    }

}
