package com.blablabber.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitLabMergeRequest {

    private String id;
    @JsonProperty("project_id")
    private String projectId;
    private String iid;
    @JsonProperty("source_branch")
    private String sourceBranch;
    @JsonProperty("source_project_id")
    private String sourceProjectId;
    @JsonProperty("target_project_id")
    private String targetProjectId;


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

    public String getSourceBranch() {
        return sourceBranch;
    }

    public void setSourceBranch(String sourceBranch) {
        this.sourceBranch = sourceBranch;
    }

    public String getSourceProjectId() {
        return sourceProjectId;
    }

    public void setSourceProjectId(String sourceProjectId) {
        this.sourceProjectId = sourceProjectId;
    }

    public String getTargetProjectId() {
        return targetProjectId;
    }

    public void setTargetProjectId(String targetProjectId) {
        this.targetProjectId = targetProjectId;
    }

    @Override
    public String toString() {
        return "GitLabMergeRequest{" + "id='" + id + '\'' +
                ", projectId='" + projectId + '\'' +
                ", iid='" + iid + '\'' +
                ", sourceBranch='" + sourceBranch + '\'' +
                ", sourceProjectId='" + sourceProjectId + '\'' +
                ", targetProjectId='" + targetProjectId + '\'' +
                '}';
    }
}
