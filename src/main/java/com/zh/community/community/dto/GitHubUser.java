package com.zh.community.community.dto;

import lombok.Data;

/**
 * created by ${host}
 */
@Data
public class GitHubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}
