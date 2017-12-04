package com.blablabber.analysis.pmd;

import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.renderers.AbstractIncrementingRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PmdRenderer extends AbstractIncrementingRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(PmdRenderer.class);
    private static final String NAME = "myRenderer";
    private List<String> violations = new ArrayList<>();

    public PmdRenderer() {
        super(NAME, "My renderer");
    }

    @Override
    public void renderFileViolations(Iterator<RuleViolation> violations) throws IOException {
        violations.forEachRemaining(ruleViolation -> {
            String x = ruleViolation.getRule().getPriority().getName() + ": " + ruleViolation.getClassName() + ":" + ruleViolation.getBeginLine()+ " - " + ruleViolation.getDescription();
            LOGGER.info("Adding violation: {}", x);
            System.out.println(x);
            //TODO redefine rules instead of checking this.
            if (!x.contains("emeter")) { // skip demeter
                this.violations.add(x);
            }
        });
    }

    @Override
    public String defaultFileExtension() {
        return "extension";
    }

    public List<String> getViolations() {
        return violations;
    }
}
