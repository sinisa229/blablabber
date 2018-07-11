package com.blablabber.analysis.sonar;

import com.google.common.annotations.Beta;
import com.sonar.sslr.api.RecognitionException;
import lombok.SneakyThrows;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;
import org.sonar.java.AnalyzerMessage;
import org.sonar.java.SonarComponents;
import org.sonar.java.ast.JavaAstScanner;
import org.sonar.java.checks.verifier.CheckVerifier;
import org.sonar.java.model.JavaVersionImpl;
import org.sonar.java.model.VisitorsBridgeForTests;
import org.sonar.java.se.checks.debug.DebugInterruptedExecutionCheck;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaVersion;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

/**
 * Stripped down version of org.sonar.java.checks.verifier.JavaCheckVerifier
 */
@Beta
public class ViolationsProvider extends CheckVerifier {

    private JavaVersion javaVersion = new JavaVersionImpl();

    @Override
    public String getExpectedIssueTrigger() {
        return "// " + ISSUE_MARKER;
    }

    @SneakyThrows
    public static Set<AnalyzerMessage> scan(String filename) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ViolationsProvider.class.getClass().getResourceAsStream("/rules.txt")))) {
            final List<String> ruleIds = reader.lines().collect(Collectors.toList());
            return getMessages(filename, new ViolationsProvider(), new RulesProvider().getRules(ruleIds));
        }
    }

    public static Set<AnalyzerMessage> scan(String filename, JavaFileScanner check) {
        return getMessages(filename, new ViolationsProvider(), singletonList(check));
    }

    public static Set<AnalyzerMessage> scan(String filename, List<JavaFileScanner> checks) {
        return getMessages(filename, new ViolationsProvider(), checks);
    }

    private static Set<AnalyzerMessage> getMessages(String filename, ViolationsProvider javaCheckVerifier, List<JavaFileScanner> checks) {
        VisitorsBridgeForTests.TestJavaFileScannerContext testJavaFileScannerContext = getTestJavaFileScannerContext(filename, javaCheckVerifier, checks);
        if (testJavaFileScannerContext.fileParsed()) {
            return testJavaFileScannerContext.getIssues();
        }
        return Collections.singleton(new AnalyzerMessage(new DebugInterruptedExecutionCheck(), new File(filename), 0, "Cannot parse file", 100));
    }

    private static VisitorsBridgeForTests.TestJavaFileScannerContext getTestJavaFileScannerContext(final String filename, final ViolationsProvider javaCheckVerifier, final List<JavaFileScanner> checks) {
        File file = new File(filename);
        SonarComponents sonarComponents = sonarComponents(file);
        VisitorsBridgeForTests visitorsBridge = new VisitorsBridgeForTests(checks, sonarComponents);
        JavaAstScanner.scanSingleFileForTests(file, visitorsBridge, javaCheckVerifier.javaVersion);
        return visitorsBridge.lastCreatedTestContext();
    }

    private static SonarComponents sonarComponents(File file) {
        SensorContextTester context = SensorContextTester.create(new File("")).setRuntime(SonarRuntimeImpl.forSonarLint(Version.create(6, 7)));
        context.setSettings(new MapSettings().setProperty("sonar.java.failOnException", true));
        SonarComponents sonarComponents = new SonarComponents(null, context.fileSystem(), null, null, null, null) {
            @Override
            public boolean reportAnalysisError(RecognitionException re, File file) {
                return false;
            }
        };
        sonarComponents.setSensorContext(context);
        context.fileSystem().add(new TestInputFileBuilder("", file.getPath()).setCharset(StandardCharsets.UTF_8).build());
        return sonarComponents;
    }

}
