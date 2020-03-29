package com.zh.community.community.controller;

import com.zh.community.community.constant.OSSClientUtil;
import com.zh.community.community.intercepter.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * created by ${host}
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private OSSClientUtil ossClientUtil;
    @Permission
    @RequestMapping("uploadImg.do")
    @ResponseBody
    public Object uploadImg(HttpServletRequest request) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        String name = ossClientUtil.uploadImg2Oss(file);
        LOGGER.info("文件上传结束：{}","img2Oss");
        String url = ossClientUtil.getImgUrl(name);
        resultMap.put("success", 1);
        resultMap.put("message", "上传成功");
        resultMap.put("url",url);
        return resultMap;
    }

    @RequestMapping("uploadLogo.do")
    @ResponseBody
    public String uploadLogo(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("logo-upload");
        String name = ossClientUtil.uploadImg2Oss(file);
        LOGGER.info("文件上传结束：{}","img2Oss");
        String url = ossClientUtil.getImgUrl(name);
        return "<script>parent.uploadPicCallbackBanner('" +url+ "');</script>";
    }
}
