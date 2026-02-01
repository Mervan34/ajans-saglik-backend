package com.ajanssaglik.controller;

import com.ajanssaglik.dto.ProductDto;
import com.ajanssaglik.model.ProductCategory;
import com.ajanssaglik.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/public/products")
@RequiredArgsConstructor
public class PublicProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getVisibleProducts() {
        return ResponseEntity.ok(productService.getVisibleProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        try {
            ProductDto product = productService.getVisibleProduct(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductDto>> filterProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        List<ProductDto> products = productService.filterVisibleProducts(category, city, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/categories")
    public ResponseEntity<ProductCategory[]> getCategories() {
        return ResponseEntity.ok(ProductCategory.values());
    }
}
