package com.blablabber.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitLabMergeRequest {

    private String id;
    @JsonProperty("project_id")
    private String projectId;
    private String iid;

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

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    @Override
    public String toString() {
        String sb = "GitLabMergeRequest{" + "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", iid='" + iid + '\'' +
                '}';
        return sb;
    }
}
