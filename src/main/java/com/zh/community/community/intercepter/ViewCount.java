package com.zh.community.community.intercepter;

import java.lang.annotation.*;

/**
 * 浏览数的aop使用
 * created by ${host}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ViewCount {
    /**
     * 浏览类型 1 问题 2 json
     * @return
     */
    int viewType() default 1;
}
