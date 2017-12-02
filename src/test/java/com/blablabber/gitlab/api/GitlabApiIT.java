package com.blablabber.gitlab.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GitlabApiIT {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8085);

    private GitlabMergeRequestProvider mergeRequestProvider = new GitlabMergeRequestProvider();

//    https://gitlab.company.com/api/v4/merge_requests?state=opened
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
        GitLabMergeRequest gitLabMergeRequest = mergeRequestProvider.getGitLabMergeRequests("http://localhost:8085").get(0);
        assertThat(gitLabMergeRequest.getProjectId(), equalTo("3"));
    }

}
