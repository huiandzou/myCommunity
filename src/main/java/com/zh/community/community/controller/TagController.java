package com.zh.community.community.controller;

import com.zh.community.community.dto.ResultDTO;
import com.zh.community.community.dto.TagDto;
import com.zh.community.community.model.Tag;
import com.zh.community.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by ${host}
 */
@Controller
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;
    @RequestMapping("/getParentTags.do")
    @ResponseBody
    public Object getParentTags(HttpServletRequest request){
        List<TagDto> parentTag = tagService.getParentTag();
        ResultDTO<List<TagDto>> listResultDTO = new ResultDTO<>();
        if(parentTag.size()<0){
            listResultDTO.setCode(1002);
        }else {
            listResultDTO.setData(parentTag);
        }
        return listResultDTO;
    }
}
