package com.zh.community.community.controller;

import com.zh.community.community.dto.PaginationDto;
import com.zh.community.community.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * created by ${host}
 */
@Controller
@RequestMapping("/index")
public class IndexController {
    private static final Logger logger=LoggerFactory.getLogger(IndexController.class);
    @Autowired
    private QuestionService questionService;

    @GetMapping("/index.do")
    public String index(HttpServletRequest request,Model model){
        logger.error("test bean ={}",questionService);
        PaginationDto paginationDto = new PaginationDto(1, 5);
        // 查询首页内容
        String sortType = request.getParameter("sort_type");
        PaginationDto result;
        if(null == sortType || "1".equals(sortType)){
            result = questionService.queryPaginationQuestions(paginationDto);
        }else if("2".equals(sortType)){
            result = questionService.queryPaginationQuestionsForHot(paginationDto);
        }else {
            result = questionService.queryPaginationQuestionsForZeroComment(paginationDto);
        }

        model.addAttribute("result", result);
        model.addAttribute("sort_type", sortType);
        return "index";
    }
    @GetMapping("/queryPagination.do")
    public String queryPagination(HttpServletRequest request,Model model,int currentPage,int pageSize){
        PaginationDto paginationDto = new PaginationDto(currentPage==0?1:currentPage, pageSize==0?5:pageSize);
        // 查询首页内容
        String sortType = request.getParameter("sort_type");
        PaginationDto result;
        if(null == sortType || "1".equals(sortType)){
            result = questionService.queryPaginationQuestions(paginationDto);
        }else if("2".equals(sortType)){
            result = questionService.queryPaginationQuestionsForHot(paginationDto);
        }else {
            result = questionService.queryPaginationQuestionsForZeroComment(paginationDto);
        }

        model.addAttribute("result", result);
        model.addAttribute("sort_type", sortType);
        return "index";
    }
}
