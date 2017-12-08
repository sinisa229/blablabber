package com.blablabber.gitlab.api;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GitlabApiClientIT {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8085);

    private GitlabApiClient mergeRequestProvider = new GitlabApiClient();
    private GitLabInfo gitLabInfo = new GitLabInfo("http://localhost:8085");

//    https://gitlab.com/api/v4/merge_requests?state=opened&scope=all
//    https://gitlab.com/api/v4/projects/498/merge_requests/1/changes
//    https://gitlab.com/api/v4/projects/498/repository/files/README.md/raw?ref=branch_name
//    https://gitlab.com/api/v4/projects/498/repository/files/OTHER_FILE.md/raw?ref=branch_name

    @Test
    public void shouldBeAbleToParseMergeRequest() throws Exception {
        stubFor(get(urlPathMatching("/api/v4/merge_requests/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("merge-requests.json")));
        GitLabMergeRequest gitLabMergeRequest = mergeRequestProvider.getAllOpenGitLabMergeRequests(gitLabInfo).get(0);
        assertThat(gitLabMergeRequest.getProjectId(), equalTo("3"));
        assertThat(gitLabMergeRequest.getIid(), equalTo("111"));
        assertThat(gitLabMergeRequest.getSourceBranch(), equalTo("test1"));
        assertThat(gitLabMergeRequest.getTargetBranch(), equalTo("master"));
        assertThat(gitLabMergeRequest.getSourceProjectId(), equalTo("2"));
        assertThat(gitLabMergeRequest.getTargetProjectId(), equalTo("3"));
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
        GitLabMergeRequestChanges mergeRequestChanges = mergeRequestProvider.getMergeRequestChanges(gitLabInfo, projectId, mergeRequestIid);
        assertThat(mergeRequestChanges.getChanges().get(0).getNewPath(), equalTo("VERSION"));
    }

    @Test
    public void shouldDownloadFile() throws Exception {
        String projectId = "4768453";
        String fileLocation = "drawer/common.go";
        String branch = "issues/3/manage_user_input";
        stubFor(get(urlEqualTo("/api/v4/projects/4768453/repository/files/drawer%2Fcommon.go/raw?ref=issues%2F3%2Fmanage_user_input"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/octet-stream")
                        .withBodyFile("someFile.txt")));
        byte[] file = mergeRequestProvider.downloadFile(gitLabInfo, projectId, fileLocation, branch);
        assertThat(new String(file), equalTo("content"));
    }

    @Test
    public void shouldPostCommentToMergeRequest() throws Exception {
        //POST /projects/:id/merge_requests/:merge_request_iid/notes
        stubFor(post(urlEqualTo("/api/v4/projects/5/merge_requests/6/notes")).willReturn(aResponse()
                .withStatus(200)));
        String projectId = "5";
        String mergeRequestIid = "6";
        mergeRequestProvider.postMergeRequestComment(gitLabInfo, projectId, mergeRequestIid);
    }
}
