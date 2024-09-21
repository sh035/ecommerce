package com.ecommerce.product.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1492017620L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.ecommerce.global.entity.QBaseTime _super = new com.ecommerce.global.entity.QBaseTime(this);

    public final com.ecommerce.category.domain.entity.QCategory childCategory;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> deliveryCharge = createNumber("deliveryCharge", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.ecommerce.image.domain.entity.Image, com.ecommerce.image.domain.entity.QImage> images = this.<com.ecommerce.image.domain.entity.Image, com.ecommerce.image.domain.entity.QImage>createList("images", com.ecommerce.image.domain.entity.Image.class, com.ecommerce.image.domain.entity.QImage.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.ecommerce.category.domain.entity.QCategory parentCategory;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final EnumPath<com.ecommerce.product.domain.enums.ProductStatus> productStatus = createEnum("productStatus", com.ecommerce.product.domain.enums.ProductStatus.class);

    public final NumberPath<Integer> qty = createNumber("qty", Integer.class);

    public final StringPath title = createString("title");

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.childCategory = inits.isInitialized("childCategory") ? new com.ecommerce.category.domain.entity.QCategory(forProperty("childCategory")) : null;
        this.parentCategory = inits.isInitialized("parentCategory") ? new com.ecommerce.category.domain.entity.QCategory(forProperty("parentCategory")) : null;
    }

}

