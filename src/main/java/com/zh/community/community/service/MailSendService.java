package com.zh.community.community.service;

import com.zh.community.community.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * created by ${host}
 */
@Service
public class MailSendService {
    @Value("${spring.mail.username}")
    private String sendName;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;
   public void sendMsg(){
       SimpleMailMessage message = new SimpleMailMessage();
       message.setFrom(sendName);
       message.setTo("1025285318@qq.com");
       message.setSubject("测试第一封java发送邮件");
       message.setText("hello world");
       javaMailSender.send(message);
   }
   public void sendMimeMessageWithValidateCode(String toEmail,String validateCode) throws MessagingException {
       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
       mimeMessageHelper.setFrom(sendName);
       mimeMessageHelper.setTo(toEmail);
       mimeMessageHelper.setSubject(Constant.EMAIL_TOPIC);
       //设置在发送给收信人之前给自己（发送方）抄送一份，不然会被当成垃圾邮件，报554错
       mimeMessage.setRecipients(MimeMessage.RecipientType.CC, InternetAddress.parse(sendName));
       Context context = new Context();
       context.setVariable("msg",validateCode);
       mimeMessageHelper.setText(templateEngine.process("EmailTemplate",context),true);
       javaMailSender.send(mimeMessage);

   }
}
