package com.ajanssaglik.repository;

import com.ajanssaglik.model.Product;
import com.ajanssaglik.model.ProductCategory;
import com.ajanssaglik.model.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatusAndVisibleTrue(ProductStatus status);

    List<Product> findByStatus(ProductStatus status);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByCategoryAndStatusAndVisibleTrue(ProductCategory category, ProductStatus status);

    List<Product> findByCity(String city);

    List<Product> findByCityAndStatusAndVisibleTrue(String city, ProductStatus status);

}