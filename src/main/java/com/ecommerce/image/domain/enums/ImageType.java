package com.ecommerce.image.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
    THUMBNAIL("썸네일"), NORMAL("일반");

    private final String value;
}
