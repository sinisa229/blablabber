package com.blablabber.gitlab.analyzer;

import com.blablabber.gitlab.api.Change;
import com.blablabber.gitlab.api.GitLabMergeRequest;
import com.blablabber.gitlab.api.GitLabMergeRequestChanges;
import com.blablabber.gitlab.api.GitlabMergeRequestProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MergeRequestFileCollector {

    private final String repositoryBaseUrl;
    private final GitlabMergeRequestProvider gitlabMergeRequestProvider;
    private final List<String> files = new ArrayList<>();
    private Path directory;

    //TODO remove this field
    private String sourceBranch;
    //TODO remove this field
    private String projectId;

    public MergeRequestFileCollector(GitlabMergeRequestProvider gitlabMergeRequestProvider, String repositoryBaseUrl) {
        this.gitlabMergeRequestProvider = gitlabMergeRequestProvider;
        this.repositoryBaseUrl = repositoryBaseUrl;
    }

    public void fetchFiles() {
        gitlabMergeRequestProvider.getAllOpenGitLabMergeRequests(repositoryBaseUrl).forEach(this::doWithMergeRequest);
    }

    private void doWithMergeRequest(GitLabMergeRequest gitLabMergeRequest) {
        System.out.println("---------------- Processing Merge request: " + gitLabMergeRequest);
        String projectId = gitLabMergeRequest.getProjectId();
        this.projectId = projectId;
        this.sourceBranch = gitLabMergeRequest.getSourceBranch();
        if (gitLabMergeRequest.getSourceProjectId().equals(gitLabMergeRequest.getTargetProjectId())) {
            doWithChanges(gitlabMergeRequestProvider.getMergeRequestChanges(repositoryBaseUrl, projectId, gitLabMergeRequest.getIid()));
        }
    }

    private void doWithChanges(GitLabMergeRequestChanges mergeRequestChanges) {
        mergeRequestChanges.getChanges().forEach(this::downloadFile);
    }

    private void downloadFile(Change change) {
        saveFile(gitlabMergeRequestProvider.downloadFile(repositoryBaseUrl, projectId, change.getNewPath(), sourceBranch));
    }

    private void saveFile(byte[] bytes) {
        try {
            System.out.println("-----------------------------------");
            System.out.println(new String(bytes));
            System.out.println("-----------------------------------");
            createDirIfNeeded();
            Path tempFile = Files.createTempFile(directory, "gitlab", null);
            Files.write(tempFile, bytes);
            files.add(tempFile.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDirIfNeeded() throws IOException {
        if (directory == null) {
            directory = Files.createTempDirectory("blablabber");
        }
    }

}
