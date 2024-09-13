package com.ecommerce.category.exception;

import lombok.Getter;

@Getter
public class NotExistParentException extends RuntimeException{

    public NotExistParentException(String message) {
        super(message);
    }

}
