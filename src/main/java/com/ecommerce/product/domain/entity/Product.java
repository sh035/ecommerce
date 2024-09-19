package com.ecommerce.product.domain.entity;

import com.ecommerce.category.domain.entity.Category;
import com.ecommerce.global.entity.BaseTime;
import com.ecommerce.image.domain.entity.Image;
import com.ecommerce.product.domain.dto.ProductUpdateDto;
import com.ecommerce.product.domain.enums.ProductStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@SQLRestriction("deleted_at IS NULL")
public class Product extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id", nullable = false)
    private Category parentCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_category_id", nullable = false)
    private Category childCategory;

    @Column
    private String name;

    @Column
    private int price;

    @Column
    private String description;

    @Column
    private int deliveryCharge;

    @Column
    private int qty;

    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    public void update(ProductUpdateDto dto, Category parentCategory, Category childCategory) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.parentCategory = parentCategory;
        this.childCategory = childCategory;
        this.deliveryCharge = dto.getDeliveryCharge();
        this.qty = dto.getQty();
        this.productStatus = dto.getProductStatus();
    }

    public void addImages(Image image) {
        images = images == null ? new ArrayList<>() : images;
        images.add(image);
    }

    public void delete(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
