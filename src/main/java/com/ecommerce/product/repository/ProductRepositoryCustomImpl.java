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
    public Slice<Product> searchKeyword(String title, String parentCategory, String childCategory,
        String sort, String sortOrder, Pageable pageable) {

        List<Product> products = queryFactory
            .selectFrom(product)
            .where(
                containsTitle(title),
                eqParentCategory(parentCategory),
                eqChildCategory(childCategory),
                product.deletedAt.isNull(),
                product.productStatus.eq(ProductStatus.SELL)
            )
            .orderBy(orderBySpecifier(sort, sortOrder))
            .limit(pageable.getPageSize() + 1)
            .offset(pageable.getOffset())
            .fetch();

        return new SliceImpl<>(products, pageable, hasNextPage(products, pageable.getPageSize()));
    }

    // TODO: title 말고 description 에 값으로 수정 해야됨
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

    private OrderSpecifier<?> orderBySpecifier(String sort, String sortOrder) {
        sort = StringUtils.hasText(sort) ? sort : "";

        return switch (sort) {
            case "PRICE" -> sortOrder.equalsIgnoreCase("DESC") ? product.price.desc()
                : product.price.asc();
            case "NEW" -> sortOrder.equalsIgnoreCase("DESC") ? product.createdAt.desc()
                : product.createdAt.asc();
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
