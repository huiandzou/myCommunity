package com.zh.community.community.service;

import com.zh.community.community.dto.NotificationDto;
import com.zh.community.community.mapper.NotificationMapper;
import com.zh.community.community.model.Notification;
import com.zh.community.community.model.NotificationExample;
import com.zh.community.community.model.Question;
import com.zh.community.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * created by ${host}
 */
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    public List<NotificationDto> myNotic(User user){
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().
                andRecevierEqualTo(Long.valueOf(user.getId())).andStatusEqualTo(0);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        List<NotificationDto> notificationDtos = notifications.stream().map(n -> {
            NotificationDto notificationDto = new NotificationDto();
            notificationDto.setId(n.getId());
            Question question = questionService.queryQuestionById(n.getOuterId());
            notificationDto.setQuestion(question);
            User userById = userService.queryUserById(n.getNotifier());
            notificationDto.setUser(userById);
            notificationDto.setType(n.getType());
            notificationDto.setGmtCreate(n.getGmtCreate());
            return notificationDto;
        }).collect(Collectors.toList());
        return notificationDtos;
    }

    public void updateNotice(String noticeId) {
        Notification notification = new Notification();
        notification.setStatus(1);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andIdEqualTo(Long.valueOf(noticeId));
        notificationMapper.updateByExampleSelective(notification, notificationExample);
    }
}
