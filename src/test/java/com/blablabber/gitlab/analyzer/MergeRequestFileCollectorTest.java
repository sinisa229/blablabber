package com.blablabber.gitlab.analyzer;

import com.blablabber.file.FileOperations;
import com.blablabber.gitlab.api.Change;
import com.blablabber.gitlab.api.GitLabMergeRequest;
import com.blablabber.gitlab.api.GitLabMergeRequestChanges;
import com.blablabber.gitlab.api.GitlabMergeRequestProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MergeRequestFileCollectorTest {

    @Mock
    private GitlabMergeRequestProvider gitlabMergeRequestProvider;

    @Mock
    private FileOperations fileOperations;

    private MergeRequestFileCollector mergeRequestFileCollector;

    @Before
    public void setUp() throws Exception {
        mergeRequestFileCollector = new MergeRequestFileCollector(gitlabMergeRequestProvider, "baseGitRepo");
        mergeRequestFileCollector.setFileOperations(fileOperations);
    }

    @Test
    public void shouldSaveFiles() throws Exception {
        doReturn(asList(getMergeRequest(gitLabMergeRequest -> {
            gitLabMergeRequest.setSourceProjectId("1");
            gitLabMergeRequest.setTargetProjectId("1");
        }))).when(gitlabMergeRequestProvider).getAllOpenGitLabMergeRequests(any());
        GitLabMergeRequestChanges toBeReturned = new GitLabMergeRequestChanges();
        toBeReturned.setChanges(asList(new Change("somePath")));
        doReturn(toBeReturned).when(gitlabMergeRequestProvider).getMergeRequestChanges(any(), any(), any());
        mergeRequestFileCollector.fetchFiles();
        verify(fileOperations).saveFile(any(), null);
    }

    private GitLabMergeRequest getMergeRequest(Consumer<GitLabMergeRequest> consumer) {
        GitLabMergeRequest gitLabMergeRequest = new GitLabMergeRequest("id", "projectId", "iid", "sourceBranch", "targetBranch", "sourceProjectId", "targetProjectId");
        consumer.accept(gitLabMergeRequest);
        return gitLabMergeRequest;
    }
}