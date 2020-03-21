package com.zh.community.community.intercepter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ${host}
 */
/*@Configuration
@ConfigurationProperties(prefix = "config.path")  //读取配置文件*/
public class InterceptorConfig implements WebMvcConfigurer {
    private List<String> normal = new ArrayList<>();

    private List<String> special = new ArrayList<>();

    private List<String> exclude = new ArrayList<>();

    public List<String> getNormal() {
        return normal;
    }

    public void setNormal(List<String> normal) {
        this.normal = normal;
    }

    public List<String> getSpecial() {
        return special;
    }

    public void setSpecial(List<String> special) {
        this.special = special;
    }

    public List<String> getExclude() {
        return exclude;
    }

    public void setExclude(List<String> exclude) {
        this.exclude = exclude;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionIntercepter())
                .excludePathPatterns(exclude)   //  不拦截
                .addPathPatterns(special);
    }
}
