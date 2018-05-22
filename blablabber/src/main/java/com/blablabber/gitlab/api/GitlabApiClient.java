package com.blablabber.gitlab.api;

import com.blablabber.BlablabberException;
import com.blablabber.gitlab.api.model.GitLabInfo;
import com.blablabber.gitlab.api.model.GitLabMergeRequest;
import com.blablabber.gitlab.api.model.GitLabMergeRequestChanges;
import lombok.SneakyThrows;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.SSLContext;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Component
public class GitlabApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabApiClient.class);
    private static final String API_V4_MERGE_REQUESTS = "/api/v4/merge_requests/";
    private static final String API_V4_PROJECTS = "/api/v4/projects/";
    private final RestTemplate restTemplate;

    public GitlabApiClient() {
        restTemplate = getRestTemplate();
    }

    @SneakyThrows
    private RestTemplate getRestTemplate() {
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        return restTemplate;
    }

    public List<GitLabMergeRequest> getMyGitLabMergeRequests(GitLabInfo gitLabInfo) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gitLabInfo.getBaseUrl() + API_V4_MERGE_REQUESTS)
                .queryParam("state", "opened");
        addOptionalToken(gitLabInfo, builder);
        ResponseEntity<List<GitLabMergeRequest>> mergeRequestList = restTemplate.exchange(builder.toUriString(), GET, null, new ParameterizedTypeReference<List<GitLabMergeRequest>>() {
        });
        return mergeRequestList.getBody();
    }

    public List<GitLabMergeRequest> getAllOpenGitLabMergeRequests(GitLabInfo gitLabInfo) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gitLabInfo.getBaseUrl() + API_V4_MERGE_REQUESTS)
                .queryParam("state", "opened")
                .queryParam("scope", "all");
        addOptionalToken(gitLabInfo, builder);
        ResponseEntity<List<GitLabMergeRequest>> mergeRequestList = restTemplate.exchange(builder.toUriString(), GET, null, new ParameterizedTypeReference<List<GitLabMergeRequest>>() {
        });
        return mergeRequestList.getBody();
    }

    public GitLabMergeRequestChanges getMergeRequestChanges(GitLabInfo gitLabInfo, String projectId, String mergeRequestIid) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gitLabInfo.getBaseUrl() + API_V4_PROJECTS + projectId + "/merge_requests/" + mergeRequestIid + "/changes");
        addOptionalToken(gitLabInfo, builder);
        ResponseEntity<GitLabMergeRequestChanges> mergeRequestList = restTemplate.exchange(builder.toUriString(), GET, null, new ParameterizedTypeReference<GitLabMergeRequestChanges>() {
        });
        return mergeRequestList.getBody();
    }

    public Optional<byte[]> downloadFile(GitLabInfo gitLabInfo, String projectId, String fileLocation, String branch) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gitLabInfo.getBaseUrl() + API_V4_PROJECTS + projectId + "/repository/files/").pathSegment(escapeSlashes(fileLocation), "raw")
                .queryParam("ref", escapeSlashes(branch));
        addOptionalToken(gitLabInfo, builder);

        URI url = builder.build(true).toUri();
        LOGGER.info(url.toString());
        try {
            ResponseEntity<byte[]> mergeRequestList = restTemplate.exchange(url, GET, null, new ParameterizedTypeReference<byte[]>() {
            });
            return Optional.of(mergeRequestList.getBody());
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                LOGGER.info("404 while getting file, probably deleted. projectId: {}, branch: {}, fileLocation:{}", projectId, branch, fileLocation);
                return Optional.empty();
            } else {
                LOGGER.error("Error while getting file. projectId: {}, branch: {}, fileLocation:{}", projectId, branch, fileLocation);
                LOGGER.error("Exception message: {}, responseBody: {}", e.getMessage(), e.getResponseBodyAsString());
                throw new BlablabberException(e);
            }
        }
    }

    public void postMergeRequestComment(GitLabInfo gitLabInfo, GitLabMergeRequest gitLabMergeRequest, String message) {
        LOGGER.info("Posting comments on {}. Comments to add: {}", gitLabMergeRequest, message);
        postMergeRequestComment(gitLabInfo, gitLabMergeRequest.getProjectId(), gitLabMergeRequest.getIid(), message);
        LOGGER.info("Posting comments on {} Done.", gitLabMergeRequest, message);
    }

    public void postMergeRequestComment(GitLabInfo gitLabInfo, String projectId, String mergeRequestIid, String message) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(gitLabInfo.getBaseUrl() + API_V4_PROJECTS + projectId + "/merge_requests/" + mergeRequestIid + "/notes");
        addOptionalToken(gitLabInfo, builder);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("body", message);
        restTemplate.postForEntity(builder.toUriString(), body, String.class);
    }

    private String escapeSlashes(String string) {
        try {
            return URLEncoder.encode(string.replaceAll(" ", "%20"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new BlablabberException(e);
        }
    }

    private void addOptionalToken(GitLabInfo gitLabInfo, UriComponentsBuilder builder) {
        if (gitLabInfo.getPrivateToken() == null) {
            return;
        }
        builder.queryParam("private_token", gitLabInfo.getPrivateToken());
    }
}
