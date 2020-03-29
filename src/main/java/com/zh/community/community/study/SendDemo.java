package com.zh.community.community.study;

import org.junit.Test;

/**
 * created by ${host}
 */
public class SendDemo {

    @Test
    public void test(){
        String SMTPHost="smtp.qq.com";
        String Port="-1";
        //直接用我的邮件进行发送测试
        String mailUsername="1025285318@qq.com";
        //密码请勿修改
        String mailPassword="phproxspdpwhbegg";
        SendMail sendMail=new SendMail(SMTPHost,Port,mailUsername,mailPassword);

        //发件人
        String mailFrom="1025285318@qq.com";
        //收件人
        String mailTo="1309227849@qq.com";
        //抄送人
        String mailCopyTo=null;
        //暗送人
        String mailBCopyTo=null;
        //邮件主题
        String mailSubject="JavaSE发送邮件测试";
        //邮件内容
        String mailBody="引用mail.jar和activation.jar实现邮件发送！";

        //发送邮件
        boolean isSend=sendMail.sendingMimeMail(mailFrom, mailTo, mailCopyTo, mailBCopyTo, mailSubject, mailBody);
        if(isSend){
            System.out.println("邮件发送成功");
        }else{
            System.out.println("邮件发送失败");
        }
    }
}
