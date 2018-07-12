package com.blablabber.gitlab.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MergeRequestInfo {

    private String id;
    private String projectName;

}
