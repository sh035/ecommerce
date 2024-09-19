package com.ecommerce.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private static final List<String> FILE_VALIDATE = List.of(".jpg", ".jpeg", ".png", ".JPG", ".JPEG", ".PNG");

    @Override
    public boolean isValid(MultipartFile multipartFile,
        ConstraintValidatorContext constraintValidatorContext) {

        String fileName = multipartFile.getOriginalFilename();

        return getFileExtension(fileName);
    }

    private boolean getFileExtension(String originalFileName) {
        return !originalFileName.isEmpty() &&
            FILE_VALIDATE.contains(originalFileName.substring(originalFileName.lastIndexOf(".")));

    }
}
