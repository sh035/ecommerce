package com.ecommerce.image.repository;

import com.ecommerce.image.domain.entity.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

  Optional<Image> findByImageName(String imageName);
}
