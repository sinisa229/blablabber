package com.blablabber.gitlab.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Change {

    @JsonProperty("new_path")
    private String newPath;

    public Change(String newPath) {
        this.newPath = newPath;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    @Override
    public String toString() {
        String sb = "Change{" + "newPath='" + newPath + '\'' +
                '}';
        return sb;
    }
}
