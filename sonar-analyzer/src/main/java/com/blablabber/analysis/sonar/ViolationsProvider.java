package com.blablabber.analysis.sonar;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import com.sonar.sslr.api.RecognitionException;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;
import org.sonar.java.AnalyzerMessage;
import org.sonar.java.SonarComponents;
import org.sonar.java.ast.JavaAstScanner;
import org.sonar.java.ast.visitors.SubscriptionVisitor;
import org.sonar.java.checks.verifier.CheckVerifier;
import org.sonar.java.model.JavaVersionImpl;
import org.sonar.java.model.VisitorsBridgeForTests;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaVersion;
import org.sonar.plugins.java.api.tree.SyntaxTrivia;
import org.sonar.plugins.java.api.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    public static Set<AnalyzerMessage> scan(String filename, JavaFileScanner check) {
        return getMessages(filename, check, new ViolationsProvider());
    }

    private static Set<AnalyzerMessage> getMessages(String filename, JavaFileScanner check, ViolationsProvider javaCheckVerifier) {
        VisitorsBridgeForTests.TestJavaFileScannerContext testJavaFileScannerContext = getTestJavaFileScannerContext(filename, check, javaCheckVerifier);
        return testJavaFileScannerContext.getIssues();
    }

    private static VisitorsBridgeForTests.TestJavaFileScannerContext getTestJavaFileScannerContext(final String filename, final JavaFileScanner check, final ViolationsProvider javaCheckVerifier) {
        JavaFileScanner expectedIssueCollector = new ExpectedIssueCollector(javaCheckVerifier);
        VisitorsBridgeForTests visitorsBridge;
        File file = new File(filename);
        SonarComponents sonarComponents = sonarComponents(file);
        visitorsBridge = new VisitorsBridgeForTests(Lists.newArrayList(check, expectedIssueCollector), sonarComponents);
        JavaAstScanner.scanSingleFileForTests(file, visitorsBridge, javaCheckVerifier.javaVersion);
        return visitorsBridge.lastCreatedTestContext();
    }

    private static class ExpectedIssueCollector extends SubscriptionVisitor {

        private final ViolationsProvider verifier;

        public ExpectedIssueCollector(ViolationsProvider verifier) {
            this.verifier = verifier;
        }

        @Override
        public List<Tree.Kind> nodesToVisit() {
            return ImmutableList.of(Tree.Kind.TRIVIA);
        }

        @Override
        public void visitTrivia(SyntaxTrivia syntaxTrivia) {
            verifier.collectExpectedIssues(syntaxTrivia.comment(), syntaxTrivia.startLine());
        }
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
