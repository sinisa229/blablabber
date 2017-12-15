package com.blablabber.gitlab.analyzer;

import com.blablabber.file.FileOperations;
import com.blablabber.gitlab.api.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.List;

import static com.blablabber.file.TestPathUtil.getMockPath;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MergeRequestFileCollectorTest {

    @Mock
    private GitlabApiClient gitlabApiClient;

    @Mock
    private FileOperations fileOperations;

    @Mock
    private MergeRequestFileCollectorListener mergeRequestFileCollectorListener;

    private GitLabMergeRequest gitLabMergeRequest;

    private MergeRequestFileCollector mergeRequestFileCollector;
    private Change change;
    @Captor
    private ArgumentCaptor<Path> directoryCaprtor;
    @Captor
    private ArgumentCaptor<String> fileNameCaptor;

    @Before
    public void setUp() throws Exception {
        mergeRequestFileCollector = new MergeRequestFileCollector(gitlabApiClient, new GitLabInfo("baseGitRepo"));
        mergeRequestFileCollector.setFileOperations(fileOperations);
        gitLabMergeRequest = getMergeRequest();
        change = new Change("change", "someOldPath");
    }

    @Test
    public void shouldDoNothingWhenSourceAndTargetIdsDoNotMatch() throws Exception {
        gitLabMergeRequest.setSourceProjectId("id");
        gitLabMergeRequest.setTargetProjectId("differentId");
        setupReturnedMergeRequests(gitLabMergeRequest);
        setupMergeRequestChanges(change);
        mergeRequestFileCollector.fetchFiles(mergeRequestFileCollectorListener);
        verifyNoMoreInteractions(fileOperations);
    }

    @Test
    public void shouldSaveFiles() throws Exception {
        gitLabMergeRequest.setSourceProjectId("id");
        gitLabMergeRequest.setTargetProjectId("id");
        setupReturnedMergeRequests(gitLabMergeRequest);
        setupMergeRequestChanges(change);
        setupDownloadFile();
        setupFileOperations("sourceDir", "targetDir");
        mergeRequestFileCollector.fetchFiles(mergeRequestFileCollectorListener);
        verify(fileOperations, times(2)).saveFile(directoryCaprtor.capture(), fileNameCaptor.capture(), any());
        assertThat(directoryCaprtor.getAllValues().get(0).toString(), equalTo("sourceDir"));
        assertThat(directoryCaprtor.getAllValues().get(1).toString(), equalTo("targetDir"));
    }

    private void setupFileOperations(final String sourceDir, String targetDir) {
        final Path sourceMockPath = getMockPath(sourceDir);
        final Path targetMockPath = getMockPath(targetDir);
        when(fileOperations.createDir(any(), any())).thenReturn(sourceMockPath).thenReturn(targetMockPath);
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
        doReturn(asList(gitLabMergeRequests)).when(gitlabApiClient).getMyGitLabMergeRequests(any());
    }

    private GitLabMergeRequest getMergeRequest() {
        return new GitLabMergeRequest("id", "projectId", "iid", "sourceBranch", "targetBranch", "sourceProjectId", "targetProjectId");
    }
}