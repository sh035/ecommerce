package com.ecommerce.image.exception;

import com.amazonaws.AmazonServiceException;

public class UploadImageFailException extends AmazonServiceException {

    public UploadImageFailException() {
        super("이미지 업로드를 실패하였습니다.");
    }
}
