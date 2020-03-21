package com.zh.community.community.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * created by ${host}
 */
@Data
public class AccessTokenDto implements Serializable {
    private static final long serialVersionUID = -8602200165695580979L;
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
