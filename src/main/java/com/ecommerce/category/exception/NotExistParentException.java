package com.ecommerce.category.exception;

import lombok.Getter;

@Getter
public class NotExistParentException extends RuntimeException{

    public NotExistParentException() {
        super("부모 카테고리가 존재하지 않습니다.");
    }

}
