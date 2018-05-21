package com.blablabber.analysis.pmd;

import com.blablabber.BlablabberException;
import net.sourceforge.pmd.PMD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class PmdAnalyzer implements com.blablabber.analysis.Analyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdAnalyzer.class);

    @Override
    public List<String> analyze(String directoryToBeScanned) {
        LOGGER.info("Starting analysis of: " + directoryToBeScanned);
        return analyze(new PmdRenderer(), directoryToBeScanned);
    }

    private List<String> analyze(PmdRenderer renderer, String fileToBeScanned) {
        PMD.doPMD(new MyPmdConfiguration(renderer, "-d", fileToBeScanned, "-f", "text", "-R", "pmdCustomRules.xml", "-r", getReportFile().toString()));
        return renderer.getViolations();
    }

    private List<String> analyzeAllPmd(PmdRenderer renderer, String fileToBeScanned) {
        PMD.doPMD(new MyPmdConfiguration(renderer, "-d", fileToBeScanned, "-f", "text", "-R", "java-basic,java-codesize,java-coupling,java-design,java-empty", "-r", getReportFile().toString()));
        return renderer.getViolations();
    }

    private Path getReportFile() {
        try {
            return Files.createTempFile("analysis", null);
        } catch (IOException e) {
            throw new BlablabberException(e);
        }
    }

}