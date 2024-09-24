package com.ecommerce.product.exception;

public class InsufficientQtyException extends RuntimeException{

    public InsufficientQtyException() {
        super("재고 수량이 부족합니다.");
    }

}
