package com.ecommerce.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

  @Value("${spring.email.host}")
  private String host;

  @Value("${spring.email.port}")
  private int port;

  @Value("${spring.email.username}")
  private String username;

  @Value("${spring.email.password}")
  private String password;

  @Bean
  public JavaMailSender mailSender() {

    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setUsername("2sh9735@gmail.com");
    mailSender.setPassword("bleydsystkepqohb"); // 구글 앱 비밀번호

    Properties javaMailProperties = new Properties();
    javaMailProperties.put("mail.transport.protocol", "smtp");
    javaMailProperties.put("mail.smtp.auth", "true");
    javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    javaMailProperties.put("mail.smtp.starttls.enable", "true");
    javaMailProperties.put("mail.debug", "true");
    javaMailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    javaMailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

    mailSender.setJavaMailProperties(javaMailProperties);

    return mailSender;
  }
}
