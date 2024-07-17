package com.ecommerce.mail.service;

import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.config.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender mailSender;
  private final RedisUtil redisUtil;
  private String authNum;

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
      MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
      helper.setFrom(from);
      helper.setTo(to);
      helper.setSubject(title);
      helper.setText(content,true);

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new CustomException(ErrorCode.EMAIL_DELIVERY_FAILED);
    }

    redisUtil.setDataExpire(authNum, to, 60 * 5L);
  }

  public String createCode() {
    Random rand = new Random();
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < 8; i++) {
      int id = rand.nextInt(3);

      switch (id) {
        case 0 -> sb.append((char) ((int) rand.nextInt(26) + 97));
        case 1 -> sb.append((char) (int) rand.nextInt(26) + 65);
        case 2 -> sb.append(rand.nextInt(9));
      }
    }
    return authNum = sb.toString();
  }

  public boolean checkAuthNum(String email, String authNum) {
    if (redisUtil.getData(authNum) == null) {
      return false;
    } else if (redisUtil.getData(authNum).equals(email)) {
      return true;
    } else {
      return false;
    }
  }

}
