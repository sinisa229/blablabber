package com.blablabber.gitlab.api;

public class GitLabInfo {
    private String baseUrl;
    private String token;

    public GitLabInfo(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public GitLabInfo(final String baseUrl, final String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "GitLabInfo{" +
                "baseUrl='" + baseUrl + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

}
