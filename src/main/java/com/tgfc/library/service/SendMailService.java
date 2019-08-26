package com.tgfc.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendMailService {

    @Autowired
    private JavaMailSender mailSender; //框架自帶的

    @Value("${spring.mail.username}")  //發送人的郵箱  比如155156641XX@163.com
    private String from;

    public void sendMail(String title, String url, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); // 發送人的郵箱
        message.setSubject(title); //標題
        message.setTo(email); //發給誰  對方郵箱
        message.setText(url); //內容
        System.out.println(from);
        mailSender.send(message); //發送
    }
}