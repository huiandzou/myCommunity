package com.zh.community.community.study;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ${host}
 */
@Service
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)
public class StudyService {
    private List<String> list = new ArrayList<>();
    private String name = "study";
    public void test(){
        name = "zouhui ";
        System.out.println("print content==name---"+name);
    }
    public void getName(){
        System.out.println("get name==" + name);
    }
}
