package com.zh.community.community.dto;

import com.zh.community.community.model.Question;
import com.zh.community.community.model.User;
import lombok.Data;

/**
 * created by ${host}
 */
@Data
public class NotificationDto {
    private Long id;
    private Integer type;
    private Long gmtCreate;
    private Question question;
    private User user;
}
