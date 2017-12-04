package com.blablabber.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Change {

    @JsonProperty("new_path")
    private String newPath;
    @JsonProperty("old_path")
    private String oldPath;

    public Change() {
        super();
    }

    public Change(String newPath, String oldPath) {
        this.newPath = newPath;
        this.oldPath = oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(final String oldPath) {
        this.oldPath = oldPath;
    }

    @Override
    public String toString() {
        return "Change{" +
                "newPath='" + newPath + '\'' +
                ", oldPath='" + oldPath + '\'' +
                '}';
    }
}
