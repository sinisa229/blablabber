package com.blablabber.controller;

import com.blablabber.gitlab.analyzer.GitLabReviewer;
import com.blablabber.gitlab.analyzer.MergeRequestAnalysisResult;
import com.blablabber.gitlab.api.model.GitLabInfo;
import com.blablabber.gitlab.api.model.MergeRequestInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gitlab/analysis/")
public class BlablabberController {

    private final GitLabReviewer gitLabReviewer;

    @SuppressWarnings("unused")
    @Autowired
    public BlablabberController(GitLabReviewer gitLabReviewer) {
        this.gitLabReviewer = gitLabReviewer;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseUrl", value = "The base url of your gitlab server. Example https://gitlab.com", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "privateToken", value = "User token. Used to identify you as a gitlab user. " +
                    "You can <a target=\"_blank\" href=\"https://docs.gitlab.com/ce/user/profile/personal_access_tokens.html\">generate one</a> if you don't have one already.", dataType = "string", paramType = "query", format = "password")
    })
    @GetMapping("preview")
    public List<MergeRequestAnalysisResult> preview(@Valid GitLabInfo gitLabInfo) {
        return gitLabReviewer.analysisPreview(gitLabInfo);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "baseUrl", value = "The base url of your gitlab server. Example https://gitlab.com", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "privateToken", value = "User token. Used to identify you as a gitlab user. " +
                    "You can <a target=\"_blank\" href=\"https://docs.gitlab.com/ce/user/profile/personal_access_tokens.html\">generate one</a> if you don't have one already.", dataType = "string", paramType = "query", format = "password")
    })
    @GetMapping("previewMergeRequest")
    public List<MergeRequestAnalysisResult> previewMergeRequest(@Valid GitLabInfo gitLabInfo, MergeRequestInfo mergeRequestInfo) {
        return gitLabReviewer.analysisPreview(gitLabInfo, mergeRequestInfo);
    }

    @GetMapping("codeReview")
    public List<MergeRequestAnalysisResult> codeReview(@Valid GitLabInfo gitLabInfo) {
        return gitLabReviewer.codeReview(gitLabInfo);
    }

}
