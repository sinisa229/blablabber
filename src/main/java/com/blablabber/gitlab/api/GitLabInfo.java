package com.blablabber.gitlab.api;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@SuppressWarnings("unused")
public class GitLabInfo {

    @NotNull
    @NotEmpty
    @URL
    private String baseUrl;
    private String privateToken;

    public GitLabInfo() {
        super();
    }

    public GitLabInfo(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public GitLabInfo(final String baseUrl, final String privateToken) {
        this.baseUrl = baseUrl;
        this.privateToken = privateToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getPrivateToken() {
        return privateToken;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setPrivateToken(String privateToken) {
        this.privateToken = privateToken;
    }

    @Override
    public String toString() {
        return "GitLabInfo{" +
                "baseUrl='" + baseUrl + '\'' +
                ", privateToken='" + privateToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitLabInfo that = (GitLabInfo) o;
        return Objects.equals(baseUrl, that.baseUrl) &&
                Objects.equals(privateToken, that.privateToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(baseUrl, privateToken);
    }
}
