package com.blablabber.gitlab.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class GitLabInfo {

    @NotNull
    @NotEmpty
    @URL
    private String baseUrl;
    private String privateToken;

    public GitLabInfo(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
