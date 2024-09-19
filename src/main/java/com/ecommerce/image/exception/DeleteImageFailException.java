package com.ecommerce.image.exception;

import com.amazonaws.AmazonServiceException;

public class DeleteImageFailException extends AmazonServiceException {

    public DeleteImageFailException() {
        super("이미지 삭제 실패하였습니다.");
    }
}
