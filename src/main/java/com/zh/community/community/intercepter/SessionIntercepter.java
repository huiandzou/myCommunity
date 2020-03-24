package com.zh.community.community.intercepter;

import com.zh.community.community.mapper.NotificationMapper;
import com.zh.community.community.mapper.UserMapper;
import com.zh.community.community.model.Notification;
import com.zh.community.community.model.NotificationExample;
import com.zh.community.community.model.User;
import com.zh.community.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * created by ${host}
 */
@Service
public class SessionIntercepter implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;

    @Autowired
    NotificationMapper notificationMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = "";
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    accessToken = cookie.getValue();
                    // 查询sql看这个token是否存在
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(accessToken);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0) {
                        NotificationExample notificationExample = new NotificationExample();
                        notificationExample.createCriteria().andRecevierEqualTo(Long.valueOf(users.get(0).getId())).andStatusEqualTo(0);
                        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);
                        // 设置session
                        request.getSession().setAttribute("gitHubUser", users.get(0));
                        if(notifications != null && notifications.size()>0){
                            request.getSession().setAttribute("noticsNum", notifications.size());
                        }
                    }
                    break;
                }
            }

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
