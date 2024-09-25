package com.ecommerce.order.exception;

public class OutOfQtyException extends RuntimeException {
    public OutOfQtyException() {
        super("구매하시려는 상품의 재고가 부족합니다.");
    }
}
