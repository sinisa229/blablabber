package com.blablabber.analysis.sonarqube;

import com.blablabber.analysis.Analyzer;
import com.blablabber.analysis.sonar.ViolationsProvider;
import org.sonar.java.AnalyzerMessage;
import org.sonar.java.checks.CatchNPECheck;
import org.sonar.plugins.java.api.JavaCheck;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SonarQubeAnalyzer implements Analyzer {

    @Override
    public List<String> analyze(final String fileToBeScanned) {
        return ViolationsProvider.scan(fileToBeScanned, new CatchNPECheck()).stream().map(analyzerMessage -> {
            final JavaCheck check = analyzerMessage.getCheck();
            return analyzerMessage.toString();
        }).collect(toList());
    }

}
