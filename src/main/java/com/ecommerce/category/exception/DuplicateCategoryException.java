package com.ecommerce.category.exception;

import lombok.Getter;

@Getter
public class DuplicateCategoryException extends RuntimeException {

    public DuplicateCategoryException(String message) {
        super(message);
    }

}
