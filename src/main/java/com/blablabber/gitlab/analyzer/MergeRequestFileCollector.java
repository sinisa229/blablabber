package com.blablabber.gitlab.analyzer;

import com.blablabber.file.FileOperations;
import com.blablabber.gitlab.api.Change;
import com.blablabber.gitlab.api.GitLabMergeRequest;
import com.blablabber.gitlab.api.GitLabMergeRequestChanges;
import com.blablabber.gitlab.api.GitlabMergeRequestProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MergeRequestFileCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeRequestFileCollector.class);
    private final String repositoryBaseUrl;
    private final GitlabMergeRequestProvider gitlabMergeRequestProvider;
    private FileOperations fileOperations;
    private Path sourceDirectory;
    private Path targetDirectory;
    private GitLabMergeRequest gitLabMergeRequest;

    public MergeRequestFileCollector(GitlabMergeRequestProvider gitlabMergeRequestProvider, String repositoryBaseUrl) {
        this.gitlabMergeRequestProvider = gitlabMergeRequestProvider;
        this.repositoryBaseUrl = repositoryBaseUrl;
        this.fileOperations = new FileOperations();
    }

    public void fetchFiles() {
        gitlabMergeRequestProvider.getAllOpenGitLabMergeRequests(repositoryBaseUrl).forEach(this::doWithMergeRequest);
    }

    private void doWithMergeRequest(GitLabMergeRequest gitLabMergeRequest) {
        makeSourceAndTargetDirectories(gitLabMergeRequest);
        this.gitLabMergeRequest = gitLabMergeRequest;

        if (gitLabMergeRequest.getSourceProjectId().equals(gitLabMergeRequest.getTargetProjectId())) {
            doWithChanges(gitlabMergeRequestProvider.getMergeRequestChanges(repositoryBaseUrl, gitLabMergeRequest.getProjectId(), gitLabMergeRequest.getIid()));
        } else {
            LOGGER.warn("Merge request between projects is not supported at the moment. Aborting processing for merge request: {}");
        }
        LOGGER.info("Processing of Merge request: {} finished.", gitLabMergeRequest);
    }

    private void doWithChanges(GitLabMergeRequestChanges mergeRequestChanges) {
        mergeRequestChanges.getChanges().forEach(this::downloadFile);
    }

    private void downloadFile(Change change) {
        downloadFile(gitLabMergeRequest.getSourceBranch(), change.getNewPath(), sourceDirectory);
        downloadFile(gitLabMergeRequest.getTargetBranch(), change.getOldPath(), targetDirectory);
    }

    private Path downloadFile(String sourceBranch, final String gitlabPath, final Path destinationDirectory) {
        byte[] bytes = gitlabMergeRequestProvider.downloadFile(repositoryBaseUrl, gitLabMergeRequest.getProjectId(), gitlabPath, sourceBranch);
        return fileOperations.saveFile(destinationDirectory, gitlabPath.replaceAll("/", "_"), bytes);
    }

    private void makeSourceAndTargetDirectories(final GitLabMergeRequest gitLabMergeRequest) {
        LOGGER.info("Processing Merge request: {}", gitLabMergeRequest);
        final List<Path> tempDirs = fileOperations.createTempDirs(gitLabMergeRequest.getProjectId() + "-" + gitLabMergeRequest.getIid(), "sourceFiles", "targetFiles");
        sourceDirectory = tempDirs.get(0);
        targetDirectory = tempDirs.get(1);
        LOGGER.debug("Created temporary merge request directories: {}", tempDirs);
    }

    public void setFileOperations(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    public Path getTargetDirectory() {
        return targetDirectory;
    }
}
