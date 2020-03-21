package com.zh.community.community.service;

import com.zh.community.community.constant.CommentTypeEnum;
import com.zh.community.community.constant.NotificationStatusEnum;
import com.zh.community.community.constant.NotificationTypeEnum;
import com.zh.community.community.dto.CommentDTO;
import com.zh.community.community.mapper.CommentMapper;
import com.zh.community.community.mapper.NotificationMapper;
import com.zh.community.community.mapper.UserMapper;
import com.zh.community.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * created by ${host}
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    NotificationMapper notificationMapper;
    @Transactional(rollbackFor = Exception.class)
    public void addComment(CommentDTO commentDTO) {
        // 直接新增评论到数据库
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        commentMapper.insert(comment);
        // 如果是评论问题
        if(commentDTO.getType().equals(CommentTypeEnum.QUESTION_COMMENT.getId())){
            Question question = questionService.queryQuestionById(commentDTO.getQuestionId());
            Notification notification = new Notification();
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setNotifier(Long.valueOf(commentDTO.getCommentator()));
            notification.setRecevier(Long.valueOf(question.getCreator()));
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setOuterId(commentDTO.getQuestionId());
            notification.setType(NotificationTypeEnum.QUESTION_ANSWER.getType());
            notificationMapper.insert(notification);
        }
        // 回复评论
        if(commentDTO.getType().equals(CommentTypeEnum.COMMENT_ANSWER.getId())){
            Comment comment1 = commentMapper.selectByPrimaryKey(commentDTO.getParentId());
            Notification notification = new Notification();
            notification.setGmtCreate(System.currentTimeMillis());
            notification.setNotifier(Long.valueOf(commentDTO.getCommentator()));
            notification.setRecevier(Long.valueOf(comment1.getCommentator()));
            notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
            notification.setOuterId(commentDTO.getQuestionId());
            notification.setType(NotificationTypeEnum.COMMENT_ANSWER.getType());
            notificationMapper.insert(notification);
        }
    }

    public List<CommentDTO> list(Integer questionId) {
        // 首先根据问题id查找评论 type = 1
        List<CommentDTO> commentDTOS = new ArrayList<>();
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(Long.valueOf(questionId))
                .andTypeEqualTo(CommentTypeEnum.QUESTION_COMMENT.getId());
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size() == 0){
            return commentDTOS;
        }
        // 获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator().intValue()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人 并成map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> integerUserMap = users.stream().collect(Collectors.toMap(user -> user.getId().toString(), user -> user));

        //转换 comment  为commentDto
        List<CommentDTO> commentDTOS1 = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(integerUserMap.get(comment.getCommentator().toString()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS1;
    }

    public List<CommentDTO> listAnswers(Long parentId,Integer type) {
        // 首先根据回复id查找二级评论 type = 2
        List<CommentDTO> commentDTOS = new ArrayList<>();
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria().andParentIdEqualTo(parentId)
                .andTypeEqualTo(CommentTypeEnum.COMMENT_ANSWER.getId());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size() == 0){
            return commentDTOS;
        }
        // 获取去重的评论人
        Set<Integer> commentators = comments.stream().map(comment -> comment.getCommentator().intValue()).collect(Collectors.toSet());
        List<Integer> userIds = new ArrayList<>();
        userIds.addAll(commentators);
        //获取评论人 并成map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<String, User> integerUserMap = users.stream().collect(Collectors.toMap(user -> user.getId().toString(), user -> user));

        //转换 comment  为commentDto
        List<CommentDTO> commentDTOS1 = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(integerUserMap.get(comment.getCommentator().toString()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS1;
    }
}
