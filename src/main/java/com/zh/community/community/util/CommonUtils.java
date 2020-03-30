package com.zh.community.community.util;

import org.springframework.stereotype.Component;

/**
 * 类名： Utils
 * 描述：用一句话描述功能
 * 创建人：xubin
 * 创建时间：2020/3/30
 * 版本：V1.0
 * 版权所有权：
 **/
@Component
public class CommonUtils {

    /**
     * 六位随机数
     * @return
     */
    public String getRandomSixNum() {
        int rannum = (int)(Math.random()*(999999-100000+1))+100000;
        return ""+rannum;
    }

}
