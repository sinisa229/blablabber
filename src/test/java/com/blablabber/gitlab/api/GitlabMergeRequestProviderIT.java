package com.blablabber.gitlab.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitlabMergeRequestProviderIT {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8085);

    private GitlabMergeRequestProvider mergeRequestProvider = new GitlabMergeRequestProvider();
    private String baseUrl = "http://localhost:8085";

//    https://gitlab.company.com/api/v4/merge_requests?state=opened&scope=all
//    https://gitlab.company.com/api/v4/projects/498/merge_requests/1/changes
//    https://gitlab.company.com/api/v4/projects/498/repository/files/README.md/raw?ref=branch_name
//    https://gitlab.company.com/api/v4/projects/498/repository/files/OTHER_FILE.md/raw?ref=branch_name

    @Test
    public void shouldBeAbleToParseMergeRequest() throws Exception {
        stubFor(get(urlPathMatching("/api/v4/merge_requests/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("merge-requests.json")));
        GitLabMergeRequest gitLabMergeRequest = mergeRequestProvider.getAllOpenGitLabMergeRequests(baseUrl).get(0);
        assertThat(gitLabMergeRequest.getProjectId(), equalTo("3"));
        assertThat(gitLabMergeRequest.getIid(), equalTo("111"));
    }

    @Test
    public void shouldParseMergeRequestChanges() throws Exception {
        stubFor(get(urlPathMatching("/api/v4/projects/498/merge_requests/12/changes"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("merge-request-changes.json")));
        String projectId = "498";
        String mergeRequestIid = "12";
        GitLabMergeRequestChanges mergeRequestChanges = mergeRequestProvider.getMergeRequestChanges(baseUrl, projectId, mergeRequestIid);
        assertThat(mergeRequestChanges.getChanges().get(0).getNewPath(), equalTo("VERSION"));
    }
}
