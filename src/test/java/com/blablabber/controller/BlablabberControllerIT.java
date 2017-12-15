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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

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
        verifyStatus(getValidRequest(), 200);
        verify(gitLabReviewer).analysisPreview(new GitLabInfo("http://someurl.com", "someSecretToken"));
    }

    @Test
    public void shouldCallPreviewAnalysisWhenTokenNotPassed() throws Exception {
        verifyStatus(getValidRequest().with(removedParameter("privateToken")), 200);
        verify(gitLabReviewer).analysisPreview(new GitLabInfo("http://someurl.com"));
    }

    @Test
    public void shouldReturn400onMissingBaseUrl() throws Exception {
        verifyStatus(getValidRequest().with(removedParameter("baseUrl")), 400);
    }

    @Test
    public void shouldReturn400onEmptyBaseUrl() throws Exception {
        verifyStatus(getRequest("", "someSecretToken"), 400);
    }

    @Test
    public void shouldReturn400onInvalidUrl() throws Exception {
        verifyStatus(getRequest("invalidUrl", "someSecretToken"), 400);
    }

    private MockHttpServletRequestBuilder getValidRequest() {
        return getRequest("http://someurl.com", "someSecretToken");
    }

    private MockHttpServletRequestBuilder getRequest(final String gitlabBaseUrl, final String someSecretToken) {
        return get(GET_URL)
                .param("baseUrl", gitlabBaseUrl)
                .param("privateToken", someSecretToken);
    }

    private ResultActions verifyStatus(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, int expectedStatus) throws Exception {
        return mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().is(expectedStatus));
    }

    private RequestPostProcessor removedParameter(final String parameterToRemove) {
        return mockHttpServletRequest -> {
            mockHttpServletRequest.removeParameter(parameterToRemove);
            return mockHttpServletRequest;
        };
    }
}