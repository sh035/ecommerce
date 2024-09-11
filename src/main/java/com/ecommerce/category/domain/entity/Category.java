package com.ecommerce.category.domain.entity;

import com.ecommerce.category.domain.dto.CategoryUpdateDto;
import com.ecommerce.global.entity.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long parentId;

    @OneToMany(mappedBy = "parentId")
    private List<Category> children = new ArrayList<>();

    public static Category root() {
        return new Category();
    }

    public void addChild(Category child) {
        children.add(child);
    }

    public void update(CategoryUpdateDto dto) {
        this.name = dto.getName();
        this.parentId = dto.getParentId();
    }
}