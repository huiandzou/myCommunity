package com.zh.community.community.controller;

import com.zh.community.community.dto.TagDto;
import com.zh.community.community.model.Question;
import com.zh.community.community.model.User;
import com.zh.community.community.service.QuestionService;
import com.zh.community.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by ${host}
 */
@RequestMapping("/publish")
@Controller
public class PublishController {
    @Autowired
    QuestionService questionService;
    @Autowired
    private TagService tagService;
    @RequestMapping("/publish.do")
    public String toPublish(Model model){
        // 添加时候把标签内容带上
        List<TagDto> parentTag = tagService.getParentTag();
        model.addAttribute("tags", parentTag);
        return "publish";
    }
    @RequestMapping("/submit.do")
    public String publish(Question question, HttpServletRequest request, Model model) {
        if (StringUtils.isEmpty(question.getTitle()) || StringUtils.isEmpty(question.getDescription()) || StringUtils.isEmpty(question.getTag())) {
            model.addAttribute("error", "参数不能为空");
            return "publish";
        }
        User user = (User) request.getSession().getAttribute("gitHubUser");
        try {
            question.setCreator(user.getId());
            questionService.insertOrUpdate(question);
        } catch (Exception e) {
            System.out.println("exception="+e);
            model.addAttribute("error", "内部添加失败");
            model.addAttribute("question", question);
           return "publish";
        }
        return "redirect:/index/index.do";
    }
}
