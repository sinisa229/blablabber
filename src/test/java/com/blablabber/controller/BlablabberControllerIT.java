package com.blablabber.controller;

import com.blablabber.gitlab.analyzer.GitLabReviewer;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BlablabberControllerIT {

    private static final String GET_URL = "/gitlab/analysis/preview";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GitLabReviewer gitLabReviewer;

    @Test
    public void shouldCallPreviewAnalysis() throws Exception {
        verifyStatus("http://someurl.com", "someSecretToken", 200);
        verify(gitLabReviewer).analysisPreview(new GitLabInfo("http://someurl.com", "someSecretToken"));
    }

    @Test
    public void shouldReturn400onEmptyBaseUrl() throws Exception {
        verifyStatus("", "someSecretToken", 400);
    }

    @Test
    public void shouldReturn400onInvalidUrl() throws Exception {
        verifyStatus("invalidUrl", "someSecretToken", 400);
    }

    private void verifyStatus(String gitlabBaseUrl, String someSecretToken, int expectedStatus) throws Exception {
        mockMvc.perform(get(GET_URL)
                .param("baseUrl", gitlabBaseUrl)
                .param("privateToken", someSecretToken))
                .andDo(print())
                .andExpect(status().is(expectedStatus));
    }
}