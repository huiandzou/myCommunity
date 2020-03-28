package com.zh.community.community.controller;

import com.zh.community.community.dto.PaginationDto;
import com.zh.community.community.model.User;
import com.zh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * created by ${host}
 */
@RequestMapping("/myQuestion")
@Controller
public class MyQuestionController {
    @Autowired
    private QuestionService questionService;

    @RequestMapping("/index.do")
    public String index(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("gitHubUser");
        if(null == user){
            return "redirect:/index/index.do";
        }
        PaginationDto paginationDto = new PaginationDto(1, 5);
        // 查询首页内容
        PaginationDto result = questionService.queryMyQuestions(user.getId(),paginationDto);
        model.addAttribute("result", result);
        return "myFocusQuestion";
    }

    @GetMapping("/queryPagination.do")
    public String queryPagination(HttpServletRequest request,Model model,int currentPage,int pageSize){
        User user = (User) request.getSession().getAttribute("gitHubUser");
        if(null == user){
            return "redirect:/index/index.do";
        }
        PaginationDto paginationDto = new PaginationDto(currentPage==0?1:currentPage, pageSize==0?5:pageSize);
        // 查询首页内容
        PaginationDto result = questionService.queryMyQuestions(user.getId(),paginationDto);
        model.addAttribute("result", result);
        return "myFocusQuestion";
    }
}
