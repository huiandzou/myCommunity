package com.zh.community.community.constant;

/**
 * created by ${host}
 */
public enum CommentTypeEnum {
    QUESTION_COMMENT(1,"问题"), COMMENT_ANSWER(2, "回复评论");
    private Integer id;
    private String comment;
    CommentTypeEnum(Integer id,String comment) {
        this.id = id;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }
}
