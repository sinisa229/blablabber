package com.blablabber.controller;

import com.blablabber.gitlab.analyzer.GitLabAnalyzer;
import com.blablabber.gitlab.api.GitLabInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gitlab/analysis/")
public class BlablabberController {

    private GitLabAnalyzer gitLabAnalyzer;

    @Autowired
    public BlablabberController(GitLabAnalyzer gitLabAnalyzer) {
        this.gitLabAnalyzer = gitLabAnalyzer;
    }

    @GetMapping("preview")
    public void preview(GitLabInfo gitLabInfo) {
        gitLabAnalyzer.analysisPreview(gitLabInfo);
    }

    //TODO add endpoint for adding comments to MRs

}
