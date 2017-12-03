package com.blablabber;

import com.blablabber.gitlab.analyzer.GitLabAnalyzer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlablabberApplicationTests {

	@Autowired
    private ApplicationContext applicationContext;

	@Autowired
    private GitLabAnalyzer gitLabAnalyzer;

	@Test
	public void contextLoads() {
        assertNotNull(applicationContext);
	}

	@Ignore
    @Test
    public void name() throws Exception {
        gitLabAnalyzer.startAnalysis("https://gitlab.com");
    }
}
