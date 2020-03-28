package com.zh.community.community.controller;

import com.zh.community.community.dto.CommentDTO;
import com.zh.community.community.dto.ResultDTO;
import com.zh.community.community.exception.CustomizeErrorCodeEnum;
import com.zh.community.community.exception.CustomizeException;
import com.zh.community.community.intercepter.Permission;
import com.zh.community.community.model.User;
import com.zh.community.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by ${host}
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Permission
    @RequestMapping("/add.do")
    @ResponseBody
    public Object addComment(HttpServletRequest request, @RequestBody CommentDTO commentDTO) {
        // 登入校验
        Object gitHubUser = request.getSession().getAttribute("gitHubUser");
        if (null == gitHubUser) {
            throw new CustomizeException(CustomizeErrorCodeEnum.NOT_LOGING);
        }
        ResultDTO resultDTO = new ResultDTO();
        // 基本参数校验
        if (validateParam(commentDTO)) {
            resultDTO.setCode(1002);
            resultDTO.setMessage("您的评论参数有问题！");
            return resultDTO;
        }
        // 组装数据
        User user = (User) request.getSession().getAttribute("gitHubUser");
        commentDTO.setCommentator(Long.valueOf(user.getId()));
        commentDTO.setGmtCreate(System.currentTimeMillis());
        commentDTO.setGmtModified(System.currentTimeMillis());
        commentService.addComment(commentDTO);
        // 成功
        return resultDTO;
    }

    @Permission
    @RequestMapping("/queryAnswers.do")
    @ResponseBody
    public Object queryAnswers(HttpServletRequest request, @RequestBody CommentDTO commentDTO) {
        // 登入校验
        Object gitHubUser = request.getSession().getAttribute("gitHubUser");
        if (null == gitHubUser) {
            throw new CustomizeException(CustomizeErrorCodeEnum.NOT_LOGING);
        }
        ResultDTO resultDTO = new ResultDTO();
        //查询二级回复评论
        List<CommentDTO> commentDTOS = commentService.listAnswers(commentDTO.getParentId(), commentDTO.getType());
        if(null == commentDTOS ||commentDTOS.size()<=0){
            resultDTO.setCode(500);
        }else {
            resultDTO.setCode(200);
            resultDTO.setData(commentDTOS);
        }
        // 成功
        return resultDTO;
    }

    private boolean validateParam(CommentDTO commentDTO) {
        return StringUtils.isEmpty(commentDTO.getParentId()) || StringUtils.isEmpty(commentDTO.getContent()) || StringUtils.isEmpty(commentDTO.getType());
    }
}
