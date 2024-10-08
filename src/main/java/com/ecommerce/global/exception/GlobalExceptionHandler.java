package com.ecommerce.global.exception;

import com.ecommerce.order.exception.NotEnoughPointException;
import com.ecommerce.order.exception.OutOfQtyException;
import com.ecommerce.product.exception.InsufficientQtyException;
import com.ecommerce.category.exception.DuplicatedCategoryException;
import com.ecommerce.category.exception.NotExistParentException;
import com.ecommerce.image.exception.DeleteImageFailException;
import com.ecommerce.image.exception.UploadImageFailException;
import com.ecommerce.mail.exception.AuthDeliveryFailException;
import com.ecommerce.mail.exception.AuthMatchFailException;
import com.ecommerce.member.exception.DuplicatedEmailException;
import com.ecommerce.member.exception.DuplicatedMemberIdException;
import com.ecommerce.member.exception.DuplicatedPasswordException;
import com.ecommerce.member.exception.DuplicatedPhoneException;
import com.ecommerce.member.exception.PasswordMatchFailException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException e) {

        log.error("유효성 검사 실패", e);


        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String> errors = fieldErrors.stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {

        log.error("NoSuchElementException", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedCategoryException.class)
    public ResponseEntity<String> handleDuplicatedCategoryException(DuplicatedCategoryException e) {

        log.error("CategoryException", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(NotExistParentException.class)
    public ResponseEntity<String> handleNotExistParentException(NotExistParentException e) {

        log.error("CategoryException", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(DeleteImageFailException.class)
    public ResponseEntity<String> handleDeleteImageFailException(DeleteImageFailException e) {

        log.error("ImageException", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }

    @ExceptionHandler(UploadImageFailException.class)
    public ResponseEntity<String> handleUploadImageFailException(UploadImageFailException e) {

        log.error("ImageException", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }

    @ExceptionHandler(AuthDeliveryFailException.class)
    public ResponseEntity<String> handleAuthDeliveryFailException(AuthDeliveryFailException e) {
        log.error("MailException", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
    }

    @ExceptionHandler(AuthMatchFailException.class)
    public ResponseEntity<String> handleAuthMatchFailException(AuthMatchFailException e) {
        log.error("MailException", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<String> handleDuplicatedEmailException(DuplicatedEmailException e) {
        log.error("MemberException", e);

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedMemberIdException.class)
    public ResponseEntity<String> handleDuplicatedMemberIdException(DuplicatedMemberIdException e) {
        log.error("MemberException", e);

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedPasswordException.class)
    public ResponseEntity<String> handleDuplicatedPasswordException(DuplicatedPasswordException e) {
        log.error("MemberException", e);

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(DuplicatedPhoneException.class)
    public ResponseEntity<String> handleDuplicatedPhoneException(DuplicatedPhoneException e) {
        log.error("MemberException", e);

        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(e.getMessage());
    }

    @ExceptionHandler(PasswordMatchFailException.class)
    public ResponseEntity<String> handlePasswordMatchFailException(PasswordMatchFailException e) {
        log.error("MemberException", e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }

    @ExceptionHandler(InsufficientQtyException.class)
    public ResponseEntity<String> handleInsufficientQtyException(InsufficientQtyException e) {
        log.error("ProductException", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }

    @ExceptionHandler(NotEnoughPointException.class)
    public ResponseEntity<String> handleNotEnoughPointException(NotEnoughPointException e) {
        log.error("OrderException", e);

        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
            .body(e.getMessage());
    }

    @ExceptionHandler(OutOfQtyException.class)
    public ResponseEntity<String> handleOutOfQtyException(OutOfQtyException e) {
        log.error("OrderException", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(e.getMessage());
    }
}
