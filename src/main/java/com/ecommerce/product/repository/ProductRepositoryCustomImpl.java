package com.ecommerce.product.repository;

import static com.ecommerce.product.domain.entity.QProduct.product;

import com.ecommerce.product.domain.entity.Product;
import com.ecommerce.product.domain.enums.ProductStatus;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Product> searchKeyword( String title, String parentCategory, String childCategory,
        String sorted, Pageable pageable) {

        List<Product> products = queryFactory
            .selectFrom(product)
            .where(
                containsTitle(title),
                eqParentCategory(parentCategory),
                eqChildCategory(childCategory),
                product.deletedAt.isNull(),
                product.productStatus.eq(ProductStatus.SELL)
            )
            .orderBy(orderBySpecifier(sorted))
            .limit(pageable.getPageSize() + 1)
            .offset(pageable.getOffset())
            .fetch();

        return new SliceImpl<>(products, pageable, hasNextPage(products, pageable.getPageSize()));
    }

    // TODO: 수정 해야됨
    private BooleanExpression containsTitle(String title) {
        return StringUtils.hasText(title) ? product.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression eqParentCategory(String parentCategory) {
        return StringUtils.hasText(parentCategory) ? product.parentCategory.categoryName.eq(
            parentCategory) : null;
    }

    private BooleanExpression eqChildCategory(String childCategory) {
        return StringUtils.hasText(childCategory) ? product.childCategory.categoryName.eq(
            childCategory) : null;
    }

    // TODO: 수정해야됨
    private OrderSpecifier<?> orderBySpecifier(String sorted) {
        sorted = StringUtils.hasText(sorted) ? sorted : "";

        return switch (sorted) {
            case "lowPrice" -> product.price.asc();
            case "highPrice" -> product.price.desc();
            default -> product.createdAt.desc();
        };
    }

    private boolean hasNextPage(List<Product> products, int pageSize) {
        if (products.size() > pageSize) {
            products.remove(pageSize);
            return true;
        }
        return false;
    }
}
