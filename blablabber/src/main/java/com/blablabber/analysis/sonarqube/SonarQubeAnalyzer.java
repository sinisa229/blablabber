package com.blablabber.analysis.sonarqube;

import com.blablabber.analysis.Analyzer;
import com.blablabber.analysis.sonar.ViolationsProvider;
import lombok.SneakyThrows;
import org.sonar.plugins.java.api.JavaCheck;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class SonarQubeAnalyzer implements Analyzer {

    @Override
    @SneakyThrows
    public List<String> analyze(final String directoryToBeScanned) {
        final List<String> collect = Files.walk(Paths.get(directoryToBeScanned)).filter(path -> path.toFile().isFile()).flatMap(path -> ViolationsProvider.scan(path.toFile().getAbsolutePath()).stream().map(analyzerMessage -> {
            final JavaCheck check = analyzerMessage.getCheck();
            return analyzerMessage.toString();
        })).collect(toList());
        return collect;
    }

}
