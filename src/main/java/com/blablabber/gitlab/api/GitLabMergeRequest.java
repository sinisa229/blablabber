package com.blablabber.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class GitLabMergeRequest {

    private String id;
    @JsonProperty("project_id")
    private String projectId;
    private String iid;
    @JsonProperty("source_branch")
    private String sourceBranch;
    @JsonProperty("target_branch")
    private String targetBranch;
    @JsonProperty("source_project_id")
    private String sourceProjectId;
    @JsonProperty("target_project_id")
    private String targetProjectId;

    public GitLabMergeRequest() {
        super();
    }

    public GitLabMergeRequest(String id, String projectId, String iid, String sourceBranch, String targetBranch, String sourceProjectId, String targetProjectId) {
        this.id = id;
        this.projectId = projectId;
        this.iid = iid;
        this.sourceBranch = sourceBranch;
        this.targetBranch = targetBranch;
        this.sourceProjectId = sourceProjectId;
        this.targetProjectId = targetProjectId;
    }

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

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
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
                ", targetBranch='" + targetBranch + '\'' +
                ", sourceProjectId='" + sourceProjectId + '\'' +
                ", targetProjectId='" + targetProjectId + '\'' +
                '}';
    }
}
