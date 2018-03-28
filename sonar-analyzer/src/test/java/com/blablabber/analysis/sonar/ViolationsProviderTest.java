package com.blablabber.analysis.sonar;

import org.junit.Test;
import org.sonar.java.AnalyzerMessage;
import org.sonar.java.checks.AbstractClassNoFieldShouldBeInterfaceCheck;
import org.sonar.java.checks.verifier.JavaCheckVerifier;

import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ViolationsProviderTest {

    private static final String TEST_FILE = "src/test/files/checks/AbstractClassNoFieldShouldBeInterfaceCheck.java";

    @Test
    public void test_no_version() {
        JavaCheckVerifier.verify("src/test/files/checks/AbstractClassNoFieldShouldBeInterfaceCheck_no_version.java", new AbstractClassNoFieldShouldBeInterfaceCheck());
        final Set<AnalyzerMessage> scan = ViolationsProvider.scan("src/test/files/checks/AbstractClassNoFieldShouldBeInterfaceCheck_no_version.java", new AbstractClassNoFieldShouldBeInterfaceCheck());
        assertThat(scan.size(), equalTo(1));
    }

}