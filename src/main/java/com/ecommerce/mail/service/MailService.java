package com.ecommerce.mail.service;

import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailRedisService mailRedisService;
    private String authNum;

    @Value("${spring.email.expiration_time}")
    private int mailExpiration;

    public String sendMessage(String sendEmail) {

        authNum = createCode();

        String from = "2sh9735@gmail.com";
        String to = sendEmail;
        String title = "회원 가입 인증 이메일 입니다.";
        String content =
            "Ecommerce 방문해주셔서 감사합니다." +
                "<br><br>" +
                "인증 번호는 " + authNum + "입니다." +
                "<br>" +
                "인증번호를 제대로 입력해주세요";
        createMessage(from, to, title, content);

        return authNum;
    }

    public void createMessage(String from, String to, String title, String content) {

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(content, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException(ErrorCode.EMAIL_DELIVERY_FAILED);
        }

        mailRedisService.setDataExpire(authNum, to, mailExpiration);
    }

    public String createCode() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        return String.valueOf(1000 + rand.nextInt(9000));
    }

    public boolean checkAuthNum(String email, String authNum) {
        return email.equals(mailRedisService.getData(authNum));
    }

}
