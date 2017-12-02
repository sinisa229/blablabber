package com.blablabber.gitlab.api;

public class Change {
    private String newPath;

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
