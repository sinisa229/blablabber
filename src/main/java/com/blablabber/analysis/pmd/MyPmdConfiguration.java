package com.blablabber.analysis.pmd;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.RulePriority;
import net.sourceforge.pmd.cli.PMDCommandLineInterface;
import net.sourceforge.pmd.cli.PMDParameters;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.LanguageVersionDiscoverer;
import net.sourceforge.pmd.renderers.Renderer;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class MyPmdConfiguration extends PMDConfiguration {

    private final PMDConfiguration configuration;
    private final PmdRenderer pmdRenderer;

    public MyPmdConfiguration(PmdRenderer pmdRenderer, String... args) {
        final PMDParameters params = PMDCommandLineInterface.extractParameters(new PMDParameters(), args, "pmd");
        this.configuration = PMDParameters.transformParametersIntoConfiguration(params);
        this.pmdRenderer = pmdRenderer;
    }

    @Override
    public String getSuppressMarker() {
        return configuration.getSuppressMarker();
    }

    @Override
    public void setSuppressMarker(String suppressMarker) {
        configuration.setSuppressMarker(suppressMarker);
    }

    @Override
    public int getThreads() {
        return configuration.getThreads();
    }

    @Override
    public void setThreads(int threads) {
        configuration.setThreads(threads);
    }

    @Override
    public ClassLoader getClassLoader() {
        return configuration.getClassLoader();
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {
        configuration.setClassLoader(classLoader);
    }

    @Override
    public void prependClasspath(String classpath) throws IOException {
        configuration.prependClasspath(classpath);
    }

    @Override
    public LanguageVersionDiscoverer getLanguageVersionDiscoverer() {
        return configuration.getLanguageVersionDiscoverer();
    }

    @Override
    public void setDefaultLanguageVersion(LanguageVersion languageVersion) {
        configuration.setDefaultLanguageVersion(languageVersion);
    }

    @Override
    public void setDefaultLanguageVersions(List<LanguageVersion> languageVersions) {
        configuration.setDefaultLanguageVersions(languageVersions);
    }

    @Override
    public LanguageVersion getLanguageVersionOfFile(String fileName) {
        return configuration.getLanguageVersionOfFile(fileName);
    }

    @Override
    public String getRuleSets() {
        return configuration.getRuleSets();
    }

    @Override
    public void setRuleSets(String ruleSets) {
        configuration.setRuleSets(ruleSets);
    }

    @Override
    public RulePriority getMinimumPriority() {
        return configuration.getMinimumPriority();
    }

    @Override
    public void setMinimumPriority(RulePriority minimumPriority) {
        configuration.setMinimumPriority(minimumPriority);
    }

    @Override
    public String getInputPaths() {
        return configuration.getInputPaths();
    }

    @Override
    public void setInputPaths(String inputPaths) {
        configuration.setInputPaths(inputPaths);
    }

    @Override
    public String getInputFilePath() {
        return configuration.getInputFilePath();
    }

    @Override
    public void setInputFilePath(String inputFilePath) {
        configuration.setInputFilePath(inputFilePath);
    }

    @Override
    public String getInputUri() {
        return configuration.getInputUri();
    }

    @Override
    public void setInputUri(String inputUri) {
        configuration.setInputUri(inputUri);
    }

    @Override
    public boolean isReportShortNames() {
        return configuration.isReportShortNames();
    }

    @Override
    public void setReportShortNames(boolean reportShortNames) {
        configuration.setReportShortNames(reportShortNames);
    }

    @Override
    public Renderer createRenderer() {
        return this.pmdRenderer;
    }

    @Override
    public Renderer createRenderer(boolean withReportWriter) {
        return createRenderer();
    }

    @Override
    public String getReportFormat() {
        return configuration.getReportFormat();
    }

    @Override
    public void setReportFormat(String reportFormat) {
        configuration.setReportFormat(reportFormat);
    }

    @Override
    public String getReportFile() {
        return configuration.getReportFile();
    }

    @Override
    public void setReportFile(String reportFile) {
        configuration.setReportFile(reportFile);
    }

    @Override
    public boolean isShowSuppressedViolations() {
        return configuration.isShowSuppressedViolations();
    }

    @Override
    public void setShowSuppressedViolations(boolean showSuppressedViolations) {
        configuration.setShowSuppressedViolations(showSuppressedViolations);
    }

    @Override
    public Properties getReportProperties() {
        return configuration.getReportProperties();
    }

    @Override
    public void setReportProperties(Properties reportProperties) {
        configuration.setReportProperties(reportProperties);
    }

    @Override
    public boolean isStressTest() {
        return configuration.isStressTest();
    }

    @Override
    public void setStressTest(boolean stressTest) {
        configuration.setStressTest(stressTest);
    }

    @Override
    public boolean isBenchmark() {
        return configuration.isBenchmark();
    }

    @Override
    public void setBenchmark(boolean benchmark) {
        configuration.setBenchmark(benchmark);
    }

    @Override
    public boolean isFailOnViolation() {
        return configuration.isFailOnViolation();
    }

    @Override
    public void setFailOnViolation(boolean failOnViolation) {
        configuration.setFailOnViolation(failOnViolation);
    }

    @Override
    public boolean isRuleSetFactoryCompatibilityEnabled() {
        return configuration.isRuleSetFactoryCompatibilityEnabled();
    }

    @Override
    public void setRuleSetFactoryCompatibilityEnabled(boolean ruleSetFactoryCompatibilityEnabled) {
        configuration.setRuleSetFactoryCompatibilityEnabled(ruleSetFactoryCompatibilityEnabled);
    }

    @Override
    public String getSourceEncoding() {
        return configuration.getSourceEncoding();
    }

    @Override
    public void setSourceEncoding(String sourceEncoding) {
        configuration.setSourceEncoding(sourceEncoding);
    }

    @Override
    public boolean isDebug() {
        return configuration.isDebug();
    }

    @Override
    public void setDebug(boolean debug) {
        configuration.setDebug(debug);
    }
}
