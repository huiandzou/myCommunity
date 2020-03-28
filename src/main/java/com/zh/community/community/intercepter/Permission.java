package com.zh.community.community.intercepter;

import java.lang.annotation.*;

/**
 * created by ${host}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    /**
     * 根据方法返回类型做出不同响应 1 页面 2 json
     * @return
     */
    int returnType() default 1;
}
