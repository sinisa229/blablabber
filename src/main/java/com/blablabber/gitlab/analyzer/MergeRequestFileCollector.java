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
    private final List<Path> files = new ArrayList<>();
    private Path directory;

    //TODO remove this field
    private String sourceBranch;
    //TODO remove this field
    private String projectId;


    public MergeRequestFileCollector(GitlabMergeRequestProvider gitlabMergeRequestProvider, String repositoryBaseUrl) {
        this.gitlabMergeRequestProvider = gitlabMergeRequestProvider;
        this.repositoryBaseUrl = repositoryBaseUrl;
        this.fileOperations = new FileOperations();
    }

    public void fetchFiles() {
        gitlabMergeRequestProvider.getAllOpenGitLabMergeRequests(repositoryBaseUrl).forEach(this::doWithMergeRequest);
    }

    private void doWithMergeRequest(GitLabMergeRequest gitLabMergeRequest) {
        LOGGER.info("Processing Merge request: {}", gitLabMergeRequest);
        directory = fileOperations.createTempDir(gitLabMergeRequest.getProjectId() + "-" + gitLabMergeRequest.getIid());
        LOGGER.debug("Created temporary merge request directory: {}", directory);

        String projectId = gitLabMergeRequest.getProjectId();
        this.projectId = projectId;
        this.sourceBranch = gitLabMergeRequest.getSourceBranch();
        if (gitLabMergeRequest.getSourceProjectId().equals(gitLabMergeRequest.getTargetProjectId())) {
            doWithChanges(gitlabMergeRequestProvider.getMergeRequestChanges(repositoryBaseUrl, projectId, gitLabMergeRequest.getIid()));
        } else {
            LOGGER.warn("Merge request between projects is not supported at the moment. Aborting processing for merge request: {}");
        }
        LOGGER.info("Processing of Merge request: {} finished.", gitLabMergeRequest);
    }

    private void doWithChanges(GitLabMergeRequestChanges mergeRequestChanges) {
        mergeRequestChanges.getChanges().forEach(this::downloadFile);
    }

    private void downloadFile(Change change) {
        byte[] bytes = gitlabMergeRequestProvider.downloadFile(repositoryBaseUrl, projectId, change.getNewPath(), sourceBranch);
        files.add(fileOperations.saveFile(directory, bytes));
    }

    public void setFileOperations(FileOperations fileOperations) {
        this.fileOperations = fileOperations;
    }

}
