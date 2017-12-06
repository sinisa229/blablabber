package com.blablabber.controller;

import com.blablabber.gitlab.analyzer.GitLabAnalyzer;
import com.blablabber.gitlab.api.GitLabInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BlablabberControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitLabAnalyzer gitLabAnalyzer;

    @Test
    public void shouldCallPreviewAnalysis() throws Exception {
        mockMvc.perform(get("/gitlab/analysis/preview")
                .param("baseUrl", "http://someurl.com")
                .param("privateToken", "someSecretToken"))
                .andExpect(status().is(200));
        verify(gitLabAnalyzer).analysisPreview(new GitLabInfo("http://someurl.com", "someSecretToken"));
    }
}