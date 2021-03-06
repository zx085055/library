package com.tgfc.library.util;

import com.tgfc.library.enums.HTMLTemplateEnum;
import com.tgfc.library.response.MailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailUtil {
    private static JavaMailSender mailSender = new JavaMailSenderImpl(); //框架自帶的

    private static String from;

    @Autowired
    public MailUtil(ITemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Value("${spring.mail.host}")
    public void setHost(String setHost) {
        ((JavaMailSenderImpl) mailSender).setHost(setHost);
    }

    @Value("${spring.mail.username}")
    public void setFrom(String username) {
        from = username;
    }

    @Value("${spring.mail.username}")
    public void setUsername(String setUsername) {
        ((JavaMailSenderImpl) mailSender).setUsername(setUsername);
    }

    @Value("${spring.mail.password}")
    public void setPassword(String setPassword) {
        ((JavaMailSenderImpl) mailSender).setPassword(setPassword);
    }

    @Value("${spring.mail.port}")
    public void setPort(int setPort) {
        ((JavaMailSenderImpl) mailSender).setPort(setPort);
    }

    private static ITemplateEngine templateEngine;

    public static void sendMail(String title, String content, String email) {
        MimeMessagePreparator mailMessage = mimeMessage -> {
            Properties properties = new Properties();
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            ((JavaMailSenderImpl) mailSender).setJavaMailProperties(properties);
            MimeMessageHelper message = new MimeMessageHelper(
                    mimeMessage, true, "UTF-8");

            message.setFrom(from, "圖書管理系統");
            message.setSubject(title); //標題
            message.setTo(email); //發給誰  對方郵箱
            message.setText(content); //內容
        };
        mailSender.send(mailMessage); //發送
    }

    public static void sendHtmlMail(String title, String content, String email) {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        ((JavaMailSenderImpl) mailSender).setJavaMailProperties(properties);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
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