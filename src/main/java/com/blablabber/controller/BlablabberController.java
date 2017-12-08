package com.blablabber.controller;

import com.blablabber.gitlab.analyzer.GitLabReviewer;
import com.blablabber.gitlab.analyzer.MergeRequestAnalysisResult;
import com.blablabber.gitlab.api.GitLabInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gitlab/analysis/")
public class BlablabberController {

    private GitLabReviewer gitLabReviewer;

    @Autowired
    public BlablabberController(GitLabReviewer gitLabReviewer) {
        this.gitLabReviewer = gitLabReviewer;
    }

    /**
     * Returns the analysis results for the open merge requests of a user
     */
    @GetMapping("preview")
    public List<MergeRequestAnalysisResult> preview(@Valid GitLabInfo gitLabInfo) {
        return gitLabReviewer.analysisPreview(gitLabInfo);
    }

    @GetMapping("codeReview")
    public List<MergeRequestAnalysisResult> codeReview(@Valid GitLabInfo gitLabInfo) {
        return gitLabReviewer.codeReview(gitLabInfo);
    }

}
