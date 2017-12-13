package com.blablabber.gitlab.analyzer;

import com.blablabber.file.FileOperations;
import com.blablabber.gitlab.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.List;

class MergeRequestFileCollector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MergeRequestFileCollector.class);
    private final GitLabInfo gitLabInfo;
    private final GitlabApiClient gitlabApiClient;
    private FileOperations fileOperations;
    private Path sourceDirectory;
    private Path targetDirectory;
    private GitLabMergeRequest gitLabMergeRequest;
    private MergeRequestFileCollectorListener mergeRequestFileCollectorListener;

    public MergeRequestFileCollector(GitlabApiClient gitlabApiClient, GitLabInfo gitLabInfo) {
        this.gitlabApiClient = gitlabApiClient;
        this.gitLabInfo = gitLabInfo;
        this.fileOperations = new FileOperations();
    }

    public void fetchFiles(MergeRequestFileCollectorListener mergeRequestFileCollectorListener) {
        List<GitLabMergeRequest> allOpenGitLabMergeRequests = gitlabApiClient.getMyGitLabMergeRequests(gitLabInfo);
        fetchFiles(allOpenGitLabMergeRequests, mergeRequestFileCollectorListener);
    }

    public void fetchFiles(List<GitLabMergeRequest> mergeRequests, MergeRequestFileCollectorListener mergeRequestFileCollectorListener) {
        this.mergeRequestFileCollectorListener = mergeRequestFileCollectorListener;
        mergeRequests.forEach(this::doWithMergeRequest);
    }

    private void doWithMergeRequest(GitLabMergeRequest gitLabMergeRequest) {
        if (mergeRequestInOneProject(gitLabMergeRequest)) {
            makeSourceAndTargetDirectories(gitLabMergeRequest);
            this.gitLabMergeRequest = gitLabMergeRequest;
            doWithChanges(gitlabApiClient.getMergeRequestChanges(gitLabInfo, gitLabMergeRequest.getProjectId(), gitLabMergeRequest.getIid()));
        } else {
            LOGGER.warn("Merge request between projects is not supported at the moment. Aborting processing for merge request: {}");
        }
        LOGGER.info("Processing of Merge request: {} finished. Source files directory: {} Target files directory: {}", gitLabMergeRequest, sourceDirectory, targetDirectory);
        mergeRequestFileCollectorListener.mergeRequestProcessed(this);
    }

    private void doWithChanges(GitLabMergeRequestChanges mergeRequestChanges) {
        mergeRequestChanges.getChanges().forEach(this::downloadFile);
    }

    private void downloadFile(Change change) {
        downloadFile(gitLabMergeRequest.getSourceBranch(), change.getNewPath(), sourceDirectory);
        downloadFile(gitLabMergeRequest.getTargetBranch(), change.getOldPath(), targetDirectory);
    }

    private void downloadFile(String sourceBranch, final String gitlabPath, final Path destinationDirectory) {
        byte[] bytes = gitlabApiClient.downloadFile(gitLabInfo, gitLabMergeRequest.getProjectId(), gitlabPath, sourceBranch);
        fileOperations.saveFile(destinationDirectory, gitlabPath.replaceAll("/", "_"), bytes);
    }

    private void makeSourceAndTargetDirectories(final GitLabMergeRequest gitLabMergeRequest) {
        LOGGER.info("Processing Merge request: {}", gitLabMergeRequest);
        Path tempDirs = fileOperations.createTempDir(gitLabMergeRequest.getProjectId() + "-" + gitLabMergeRequest.getIid());
        sourceDirectory = fileOperations.createDir(tempDirs, "sourceFiles");
        targetDirectory = fileOperations.createDir(tempDirs, "targetFiles");
        LOGGER.debug("Created temporary merge request directories: {}", tempDirs);
    }

    private boolean mergeRequestInOneProject(GitLabMergeRequest gitLabMergeRequest) {
        return gitLabMergeRequest.getSourceProjectId().equals(gitLabMergeRequest.getTargetProjectId());
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

    public GitLabMergeRequest getGitLabMergeRequest() {
        return gitLabMergeRequest;
    }
}
