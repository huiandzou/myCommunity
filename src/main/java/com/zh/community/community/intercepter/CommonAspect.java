package com.zh.community.community.intercepter;

import com.alibaba.fastjson.JSON;
import com.zh.community.community.constant.RedisConstant;
import com.zh.community.community.util.RedisUtil;
import com.zh.community.community.mapper.NotificationMapper;
import com.zh.community.community.mapper.UserMapper;
import com.zh.community.community.model.Notification;
import com.zh.community.community.model.NotificationExample;
import com.zh.community.community.model.User;
import com.zh.community.community.model.UserExample;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by ${host}
 */
@Aspect
@Component
public class CommonAspect {

    @Autowired
    UserMapper userMapper;
    @Value("${github.client.id}")
    private String clientId;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    RedisUtil redisUtil;

    @Around("execution(* com.zh.community.community.controller.*.*(..))")
    public Object before(ProceedingJoinPoint pjp) {
        // 获取请求resuest参数
        HttpServletRequest httpServletRequest;
        Object[] args = pjp.getArgs();
        List<Object> collectRequest = Arrays.stream(args).filter(arg ->
                arg instanceof HttpServletRequest).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(collectRequest)) {
            httpServletRequest = (HttpServletRequest) collectRequest.get(0);
        } else {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            httpServletRequest = attributes.getRequest();
        }
        // 捞取首先注解
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Permission pm = method.getAnnotation(Permission.class);
        ViewCount viewCount = method.getAnnotation(ViewCount.class);
        if (viewCount != null) {
            String[] parameterNames = signature.getParameterNames();
            int index = 0;
            for (int i = 0; i < parameterNames.length; i++) {
                if ("id".equals(parameterNames[i])) {
                    index = i;
                   break;
                }
            }
            Integer questionId = (Integer)args[index];
            // 记录浏览数到缓存
            redisUtil.incr(RedisConstant.QUESTION_VIEW_COUNT + String.valueOf(questionId), 1);
        }
        //设置登入请求路径
        StringBuffer requestURL = httpServletRequest.getRequestURL();
        String servletPath = httpServletRequest.getServletPath();
        String domain = requestURL.substring(0, (requestURL.length() - servletPath.length()) + 1);
        System.out.println("domain=" + domain);
        String loggingPath = "https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=" + domain + "callback&scope=user&state=1";
        httpServletRequest.getSession().setAttribute("loggingPath", loggingPath);
        // 真正请求处理请处理
        boolean handleFlag = handleSession(httpServletRequest);
        // 打印下处理结果
        Object user = httpServletRequest.getSession().getAttribute("gitHubUser");
        System.out.println("session user = " + JSON.toJSONString(user));
        System.out.println("loggingPath =" + httpServletRequest.getSession().getAttribute("loggingPath"));
        // 登入状态下直接放行
        if (handleFlag) {
            try {
                Object proceed = pjp.proceed();
                return proceed;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else if (null != pm) {
            // 受限资源直接跳转登入
            String path = "redirect:" + loggingPath;
            return path;
        } else {
            try {
                Object proceed = pjp.proceed();
                return proceed;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }


    public boolean handleSession(HttpServletRequest request) {
        String accessToken = "";
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
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
                if (notifications != null && notifications.size() > 0) {
                    request.getSession().setAttribute("noticsNum", notifications.size());
                }
                return true;
            }
        }
        return false;
    }
}
