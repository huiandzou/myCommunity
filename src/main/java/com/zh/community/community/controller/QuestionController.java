package com.zh.community.community.controller;

import com.zh.community.community.dto.CommentDTO;
import com.zh.community.community.dto.QuestionDto;
import com.zh.community.community.intercepter.Permission;
import com.zh.community.community.model.Question;
import com.zh.community.community.service.CommentService;
import com.zh.community.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * created by ${host}
 */
@RequestMapping("/question")
@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;
    @RequestMapping("/{id}")
    public String queryQuestion(@PathVariable(name = "id") Integer id, Model model){
      QuestionDto questionDto = questionService.getById(id);
        List<CommentDTO> commentDTOS =  commentService.list(id);
        List<Question> recomendQuestions = questionService.quesryRecomendQuestions(questionDto);
        model.addAttribute("comments", commentDTOS);
        model.addAttribute("question", questionDto);
        model.addAttribute("relatedQuestions", recomendQuestions);
        return "question";
    }
    @Permission
    @RequestMapping("/edit/{id}")
    public String editQuestion(@PathVariable(name = "id") Integer id, Model model){
        QuestionDto questionDto = questionService.getById(id);
        model.addAttribute("question", questionDto);
        return "edit";
    }
}
