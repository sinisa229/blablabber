package com.blablabber.gitlab.api.model;

import com.blablabber.gitlab.api.model.Change;

import java.util.List;

public class GitLabMergeRequestChanges {

    private List<Change> changes;

    public List<Change> getChanges() {
        return changes;
    }

    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        return "GitLabMergeRequestChanges{" + "changes=" + changes + '}';
    }
}
