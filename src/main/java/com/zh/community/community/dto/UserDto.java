package com.zh.community.community.dto;

import lombok.Data;

/**
 * created by ${host}
 */
@Data
public class UserDto {
    private Integer id;
    private String accountId;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
    private String emailAddress;
    private String emailPassWord;
    private String validateCode;
}
