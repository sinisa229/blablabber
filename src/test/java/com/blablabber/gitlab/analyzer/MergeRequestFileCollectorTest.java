package com.blablabber.gitlab.analyzer;

import com.blablabber.file.FileOperations;
import com.blablabber.gitlab.api.Change;
import com.blablabber.gitlab.api.GitLabInfo;
import com.blablabber.gitlab.api.GitLabMergeRequest;
import com.blablabber.gitlab.api.GitLabMergeRequestChanges;
import com.blablabber.gitlab.api.GitlabApiClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MergeRequestFileCollectorTest {

    @Mock
    private GitlabApiClient gitlabApiClient;

    @Mock
    private FileOperations fileOperations;

    private GitLabMergeRequest gitLabMergeRequest;

    private MergeRequestFileCollector mergeRequestFileCollector;
    private Change somePath;

    @Before
    public void setUp() throws Exception {
        mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, new GitLabInfo("baseGitRepo"));
        mergeRequestFileCollector.setFileOperations(fileOperations);
        gitLabMergeRequest = getMergeRequest();
        somePath = new Change("somePath", "someOldPath");
    }

    @Test
    public void shouldSaveFiles() throws Exception {
        gitLabMergeRequest.setSourceProjectId("id");
        gitLabMergeRequest.setTargetProjectId("id");
        setupReturnedMergeRequests(gitLabMergeRequest);
        setupMergeRequestChanges(somePath);
        setupDownloadFile();
        setupFileOperations("sourceDir", "targetDir");
        mergeRequestFileCollector.fetchFiles();
        //TODO add context to this assertion
        verify(fileOperations, times(2)).saveFile(any(), any(), any());
    }

    private void setupFileOperations(final String sourceDir, String targetDir) {
        final Path sourceMockPath = getMockPath(sourceDir);
        final Path targetMockPath = getMockPath(targetDir);
        doReturn(asList(sourceMockPath, targetMockPath)).when(fileOperations).createDir(any(), any());
    }

    private Path getMockPath(final String sourceDir) {
        final Path mock = mock(Path.class);
        doReturn(sourceDir).when(mock).toString();
        return mock;
    }

    private void setupDownloadFile() {
        doReturn(new byte[1]).when(gitlabApiClient).downloadFile(any(), anyString(), anyString(), anyString());
    }

    private void setupMergeRequestChanges(final Change... somePath1) {
        GitLabMergeRequestChanges toBeReturned = getGitLabMergeRequestChanges(somePath1);
        setupMergeRequestChanges(toBeReturned);
    }

    private void setupMergeRequestChanges(final GitLabMergeRequestChanges toBeReturned) {
        doReturn(toBeReturned).when(gitlabApiClient).getMergeRequestChanges(any(), any(), any());
    }

    private GitLabMergeRequestChanges getGitLabMergeRequestChanges(final Change... changes) {
        GitLabMergeRequestChanges toBeReturned = new GitLabMergeRequestChanges();
        final List<Change> somePath = asList(changes);
        toBeReturned.setChanges(somePath);
        return toBeReturned;
    }

    private void setupReturnedMergeRequests(GitLabMergeRequest... gitLabMergeRequests) {
        doReturn(asList(gitLabMergeRequests)).when(gitlabApiClient).getAllOpenGitLabMergeRequests(any());
    }

    private GitLabMergeRequest getMergeRequest(Consumer<GitLabMergeRequest>... consumer) {
        GitLabMergeRequest gitLabMergeRequest = new GitLabMergeRequest("id", "projectId", "iid", "sourceBranch", "targetBranch", "sourceProjectId", "targetProjectId");
        Arrays.stream(consumer).forEach(gitLabMergeRequestConsumer -> gitLabMergeRequestConsumer.accept(gitLabMergeRequest));
        return gitLabMergeRequest;
    }
}