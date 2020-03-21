package com.zh.community.community.controller;

import com.zh.community.community.dto.AccessTokenDto;
import com.zh.community.community.dto.GitHubUser;
import com.zh.community.community.model.User;
import com.zh.community.community.provider.GitHubProvider;
import com.zh.community.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * created by ${host}
 */
@Controller
public class AuthorizeController {
    @Autowired
    GitHubProvider gitHubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;
    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code, @RequestParam(name = "state") String state, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDto);
        GitHubUser gitHubUser = gitHubProvider.getUser(accessToken);
        System.out.println(gitHubUser.getName());
        if (gitHubUser != null) {
            // 登入成功 写session
            User user1 = new User();
            user1.setToken(accessToken);
            user1.setName(gitHubUser.getName());
            user1.setAccountId(String.valueOf(gitHubUser.getId()));
            user1.setAvatarUrl(gitHubUser.getAvatar_url());
            userService.createOrUpdate(user1);
            httpServletResponse.addCookie(new Cookie("token",accessToken));
            return "redirect:/index/index.do";
        } else {
            //登入失败
            return "redirect:/index/index.do";
        }
    }

    @RequestMapping("/logOut")
    public String logOut(HttpServletRequest request,HttpServletResponse response){
        // 删除session
        request.getSession().removeAttribute("gitHubUser");

        // 删除cookie
        response.addCookie(new Cookie("token",null));
        return "redirect:/index/index.do";
    }
}
