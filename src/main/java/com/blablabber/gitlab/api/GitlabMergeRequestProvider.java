package com.blablabber.gitlab.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class GitlabMergeRequestProvider {

    public static final String API_V4_MERGE_REQUESTS = "/api/v4/merge_requests/";
    private static final String API_V4_PROJECTS = "/api/v4/projects/";

    public List<GitLabMergeRequest> getMyGitLabMergeRequests(String baseUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<GitLabMergeRequest>> mergeRequestList = restTemplate.exchange(baseUrl + API_V4_MERGE_REQUESTS, GET, null, new ParameterizedTypeReference<List<GitLabMergeRequest>>(){});
        return mergeRequestList.getBody();
    }

    public List<GitLabMergeRequest> getAllOpenGitLabMergeRequests(String baseUrl) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + API_V4_MERGE_REQUESTS)
                .queryParam("state", "opened")
                .queryParam("scope", "all");
        ResponseEntity<List<GitLabMergeRequest>> mergeRequestList = restTemplate.exchange(builder.toUriString(), GET, null, new ParameterizedTypeReference<List<GitLabMergeRequest>>(){});
        return mergeRequestList.getBody();
    }


    public GitLabMergeRequestChanges getMergeRequestChanges(String baseUrl, String projectId, String mergeRequestIid) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + API_V4_PROJECTS + projectId + "/merge_requests/" + mergeRequestIid + "/changes");
        ResponseEntity<GitLabMergeRequestChanges> mergeRequestList = restTemplate.exchange(builder.toUriString(), GET, null, new ParameterizedTypeReference<GitLabMergeRequestChanges>(){});
        return mergeRequestList.getBody();
    }
}
