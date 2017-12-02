package com.blablabber.gitlab.api;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class GitlabMergeRequestProvider {

    public List<GitLabMergeRequest> getGitLabMergeRequests(String baseUrl) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<GitLabMergeRequest>> mergeRequestList = restTemplate.exchange(baseUrl + "/api/v4/merge_requests/", GET, null, new ParameterizedTypeReference<List<GitLabMergeRequest>>(){});
        return mergeRequestList.getBody();
    }

}
