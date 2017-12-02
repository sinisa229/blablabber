package com.blablabber.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitLabMergeRequest {

    private String id;
    @JsonProperty("project_id")
    private String projectId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "GitLabMergeRequest{" + "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }

}
