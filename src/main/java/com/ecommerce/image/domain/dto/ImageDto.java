package com.ecommerce.image.domain.dto;

import com.ecommerce.image.domain.entity.Image;
import com.ecommerce.image.domain.enums.ImageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageDto {

    private Long id;
    private Long productId;
    private String imageUrl;
    private ImageType imageType;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/seoul")
    private LocalDateTime createdAt;

    public static ImageDto from(Image image) {
        return ImageDto.builder()
            .id(image.getId())
            .productId(image.getProduct() != null ? image.getProduct().getId() : null)
            .imageUrl(image.getImageUrl())
            .imageType(image.getImageType())
            .createdAt(image.getCreatedAt())
            .build();
    }
}
