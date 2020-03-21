package com.zh.community.community.controller;

import com.zh.community.community.dto.NotificationDto;
import com.zh.community.community.mapper.NotificationMapper;
import com.zh.community.community.model.Notification;
import com.zh.community.community.model.NotificationExample;
import com.zh.community.community.model.User;
import com.zh.community.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * created by ${host}
 */
@Controller
@RequestMapping("/notic")
public class NotificationController {

    @Autowired
   private NotificationService notificationService;
    @Autowired
    NotificationMapper notificationMapper;
    @RequestMapping("/myNotice.do")
    public String myNotice(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("gitHubUser");
        if(null == user){
            return "redirect:/index/index.do";
        }
        // 根据当前登入用户获取通知内容
        List<NotificationDto> notificationDtos = notificationService.myNotic(user);
        model.addAttribute("notices", notificationDtos);
        return "notification";
    }

    @RequestMapping("/updateNoticeStatus.do")
    public String updateNoticeStatus(HttpServletRequest request,String noticeId,String questionId){
        notificationService.updateNotice(noticeId);
        User gitHubUser = (User) request.getSession().getAttribute("gitHubUser");
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andRecevierEqualTo(Long.valueOf(gitHubUser.getId())).andStatusEqualTo(0);
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
        if(notifications != null && notifications.size()>0){
            request.getSession().setAttribute("noticsNum", notifications.size());
        }else {
            request.getSession().removeAttribute("noticsNum");
        }
        return "redirect:/question/"+questionId;
    }
}
