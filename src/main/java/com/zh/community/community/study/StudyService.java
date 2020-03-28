package com.zh.community.community.study;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ${host}
 */
/*@Service
@Scope(ConfigurableListableBeanFactory.SCOPE_PROTOTYPE)*/
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
