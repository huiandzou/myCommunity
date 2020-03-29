package com.zh.community.community.controller;

import com.zh.community.community.constant.Constant;
import com.zh.community.community.dto.ResultDTO;
import com.zh.community.community.dto.UserDto;
import com.zh.community.community.model.User;
import com.zh.community.community.service.MailSendService;
import com.zh.community.community.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 登入账号选择邮箱注册登入 github第三方登入
 * created by ${host}
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    private static Logger LOGGER = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private MailSendService mailSendService;
    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String goToLogin(){
        return "login";
    }
    @RequestMapping("/register")
    public String register(){
        return "register";
    }
    @RequestMapping("/signIn.do")
    @ResponseBody
    public Object signIn(HttpServletRequest request, HttpServletResponse response, UserDto userDto) {
        ResultDTO resultDTO = new ResultDTO();
        if (null == userDto) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("登入账号或密码不对");
            return resultDTO;
        }
        User user = userService.queryUserByLogin(userDto);
        if (null == user) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("登入账号或密码不对");
            return resultDTO;
        }
        // 设置缓存token
        String uuid = uuid();
        user.setToken(uuid);
        user.setGmtModified(System.currentTimeMillis());
        boolean flag = userService.signIn(user);
        if (!flag) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("登入账号或密码不对");
            return resultDTO;
        }
        Cookie cookie = new Cookie("token", uuid);
        cookie.setMaxAge(Constant.WEEK_TIME);
        cookie.setPath("/community");
        response.addCookie(cookie);
        return resultDTO;
    }

    @RequestMapping("/register.do")
    @ResponseBody
    public Object register(HttpServletRequest request, HttpServletResponse response, UserDto userDto) {
        ResultDTO resultDTO = new ResultDTO();
        // 基本参数验证
        if (!validateParam(userDto, resultDTO)) {
            return resultDTO;
        }

        // 校验验证码匹配
        Cookie[] cookies = request.getCookies();
        List<Cookie> cookieList = Arrays.stream(cookies).filter(c -> "code".equals(c.getName())).collect(Collectors.toList());
        if (cookieList.size() > 0) {
            Cookie cookie = cookieList.get(0);
            String name = cookie.getValue();
            if (!name.equals(userDto.getValidateCode())) {
                resultDTO.setCode(1001);
                resultDTO.setMessage("验证码输入不对");
                return resultDTO;
            }

        }else {
            resultDTO.setCode(1001);
            resultDTO.setMessage("请重新获取验证码");
            return resultDTO;
        }
        // 是否有过注册验证
        User user = userService.queryUserByEmail(userDto);
        if (null != user) {
            resultDTO.setCode(1002);
            resultDTO.setMessage("已经注册过，请直接登入");
            return resultDTO;
        }

        // 直接入库
        boolean flag = userService.register(userDto);

        if (!flag) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("注册失败");
            return resultDTO;
        }
        return resultDTO;
    }

    private boolean validateParam(UserDto userDto, ResultDTO resultDTO) {
        if (null == userDto) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("注册参数不对");
            return false;
        }
        if (!valiadteEmail(resultDTO, userDto.getEmailAddress())) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("邮箱不对");
            return false;
        }
        if (StringUtils.isEmpty(userDto.getName())) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("用户名不能为空");
            return false;
        }
        if (StringUtils.isEmpty(userDto.getValidateCode())) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("验证码不能为空");
            return false;
        }
       /* if (StringUtils.isEmpty(userDto.getAvatarUrl())) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("头像部不能为空");
            return false;
        }*/
        if (StringUtils.isEmpty(userDto.getEmailPassWord())) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("密码不能为空");
            return false;
        }
        return true;
    }

    @RequestMapping("/sendEmailValidate.do")
    @ResponseBody
    public Object sendEmailValidate(HttpServletRequest request, HttpServletResponse response) {
        ResultDTO resultDTO = new ResultDTO();
        String email = request.getParameter("email");
        if (!valiadteEmail(resultDTO, email)) {
            return resultDTO;
        }
        // 随机数字生成正确邮箱  然后发送邮件
        StringBuilder stringBuilder = new StringBuilder();
        this.generateCode(stringBuilder);
        try {
            mailSendService.sendMimeMessageWithValidateCode(email, stringBuilder.toString());
        } catch (MessagingException e) {
            LOGGER.error("sendEmailValidate fail error:{}", e);
            resultDTO.setCode(1001);
            resultDTO.setMessage("发送验证码失败，请重新发送");
            return resultDTO;
        }

        // 设置缓存验证码时间 60秒 后期加上redis缓存使用
        Cookie cookie = new Cookie("code", stringBuilder.toString());
        cookie.setMaxAge(360);
        response.addCookie(cookie);
        return resultDTO;
    }

    private boolean valiadteEmail(ResultDTO resultDTO, String email) {
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(email);
        if (StringUtils.isEmpty(email) || !m.matches()) {
            resultDTO.setCode(1001);
            resultDTO.setMessage("邮箱格式不正确!");
            return false;
        }
        return true;
    }

    public void generateCode(StringBuilder stringBuilder) {
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10));
        }
    }

    public String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
