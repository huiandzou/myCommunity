package com.zh.community.community.dto;

import com.zh.community.community.model.User;
import lombok.Data;

/**
 * created by ${host}
 */
@Data
public class QuestionDto {
    private Integer id;
    private String title;
    private String description;
    private Integer creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer praiseCount;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private User user;
}
