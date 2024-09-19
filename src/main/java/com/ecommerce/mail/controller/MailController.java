package com.ecommerce.mail.controller;

import com.ecommerce.mail.service.MailService;
import com.ecommerce.mail.dto.EmailCheckDto;
import com.ecommerce.mail.dto.EmailRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mail")
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> emailSend(@RequestBody @Valid EmailRequestDto dto) {
        mailService.sendMessage(dto.getEmail());

        return ResponseEntity.status(HttpStatus.OK)
            .body("인증번호가 전송되었습니다.");
    }

    @PostMapping("/check")
    public ResponseEntity<Boolean> authCheck(@RequestBody @Valid EmailCheckDto dto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(mailService.checkAuthNum(dto.getEmail(), dto.getAuthNum()));
    }
}
