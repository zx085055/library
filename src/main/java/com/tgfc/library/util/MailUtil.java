package com.tgfc.library.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class MailUtil {

    private static JavaMailSender mailSender = new JavaMailSenderImpl(); //框架自帶的

    @Value("${spring.mail.username}")  //發送人的郵箱  比如155156641XX@163.com
    private static String from;

    public static void sendMail(String title, String context, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 發送人的郵箱
        message.setSubject(title); //標題
        message.setTo(email); //發給誰  對方郵箱
        message.setText(context); //內容
        System.out.println(from);
        mailSender.send(message); //發送
    }
}