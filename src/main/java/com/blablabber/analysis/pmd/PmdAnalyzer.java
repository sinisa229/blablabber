package com.blablabber.analysis.pmd;

import net.sourceforge.pmd.PMD;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PmdAnalyzer {

    public List<String> analyze(String fileToBeScanned) {
        return analyze(new PmdRenderer(), fileToBeScanned);
    }

    public List<String> analyze(PmdRenderer renderer, String fileToBeScanned) {
        PMD.doPMD(new MyPmdConfiguration(renderer,"-d", fileToBeScanned, "-f", "text", "-R", "java-basic,java-codesize,java-coupling,java-design,java-empty", "-r", "/home/sinisha/projects/sorcey/core/target/pmd"));
        return renderer.getViolations();
    }

}