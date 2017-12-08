package com.blablabber.controller;

import com.blablabber.gitlab.analyzer.GitLabAnalyzer;
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

    private GitLabAnalyzer gitLabAnalyzer;

    @Autowired
    public BlablabberController(GitLabAnalyzer gitLabAnalyzer) {
        this.gitLabAnalyzer = gitLabAnalyzer;
    }

    @GetMapping("preview")
    public List<MergeRequestAnalysisResult> preview(@Valid GitLabInfo gitLabInfo) {
        return gitLabAnalyzer.analysisPreview(gitLabInfo);
    }

    //TODO add endpoint for adding comments to MRs

}
