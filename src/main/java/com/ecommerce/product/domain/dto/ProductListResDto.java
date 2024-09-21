package com.ecommerce.product.domain.dto;

import com.ecommerce.image.domain.dto.ImageDto;
import com.ecommerce.product.domain.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductListResDto {

    private Long id;
    private String title;
    private int price;
    private int deliveryCharge;
    private List<ImageDto> images;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/seoul")
    private LocalDateTime createdAt;

    public static ProductListResDto from(Product product) {

        return ProductListResDto.builder()
            .id(product.getId())
            .title(product.getTitle())
            .price(product.getPrice())
            .deliveryCharge(product.getDeliveryCharge())
            .images(product.getImages().stream().map(ImageDto::from).toList())
            .createdAt(product.getCreatedAt())
            .build();
    }


}
