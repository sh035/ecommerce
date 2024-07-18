package com.ecommerce.mail.controller;

import com.ecommerce.global.exception.CustomException;
import com.ecommerce.global.exception.ErrorCode;
import com.ecommerce.mail.service.MailService;
import com.ecommerce.mail.dto.EmailCheckDto;
import com.ecommerce.mail.dto.EmailRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class MailController {

  private final MailService mailService;

  @PostMapping("/mail/send")
  public String emailSend(@RequestBody @Valid EmailRequestDto dto) throws Exception {
    log.info("이메일 인증 이메일 : " + dto.getEmail());
    return mailService.sendMessage(dto.getEmail());
  }

  @PostMapping("/mail/authcheck")
  public boolean authCheck(@RequestBody @Valid EmailCheckDto dto) {
    Boolean check = mailService.checkAuthNum(dto.getEmail(), dto.getAuthNum());
    if (check) {
      return true;
    } else {
      throw new CustomException(ErrorCode.NOT_MATCH_AUTH);
    }
  }
}
