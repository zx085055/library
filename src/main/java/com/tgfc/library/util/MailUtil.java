package com.tgfc.library.util;

import com.tgfc.library.enums.HTMLTemplateEnum;
import com.tgfc.library.response.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {

    private static JavaMailSender mailSender = new JavaMailSenderImpl(); //框架自帶的

    private static String from;

    @Value("${spring.mail.username}")  //發送人的郵箱  比如155156641XX@163.com
    private String initFrom;

    @Autowired
    public MailUtil(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @PostConstruct
    private void init() {
        from = this.initFrom;
    }

    private static ITemplateEngine templateEngine;

    public static void sendMail(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 發送人的郵箱
        message.setSubject(title); //標題
        message.setTo(email); //發給誰  對方郵箱
        message.setText(content); //內容
        System.out.println(from);
        mailSender.send(message); //發送
    }

    public static void sendHtmlMail(String title, String content, String email) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendTemplateMail(MailResponse mailResponse, String type) {
        Context context = new Context();
        context.setVariable("mailResponse", mailResponse);
        String emailContent = templateEngine.process(HTMLTemplateEnum.typeToCode(type), context);
        sendHtmlMail(mailResponse.getTitle(), emailContent, mailResponse.getEmail());
    }
}