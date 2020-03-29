package com.zh.community.community.study;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * created by ${host}
 */
public class Auth extends Authenticator {
    private String username = "";
    private String password = "";

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }

}
