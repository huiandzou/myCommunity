package com.zh.community.community.constant;

/**
 * created by ${host}
 */
public enum NotificationTypeEnum {
    QUESTION_ANSWER(1,"问题回复通知"),
    COMMENT_ANSWER(2,"评论回复通知"),
    LIKE_ANSWER(3,"点赞通知");
    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
