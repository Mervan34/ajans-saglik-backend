package com.ajanssaglik.service;

import com.ajanssaglik.dto.ProductDto;
import com.ajanssaglik.model.Product;
import com.ajanssaglik.model.ProductCategory;
import com.ajanssaglik.model.ProductStatus;
import com.ajanssaglik.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // Public endpoints - sadece visible=true olanları göster
    public List<ProductDto> getVisibleProducts() {
        return productRepository.findByStatusAndVisibleTrue(ProductStatus.ACTIVE).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getVisibleProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));

        if (!product.getVisible() || product.getStatus() != ProductStatus.ACTIVE) {
            throw new RuntimeException("Ürün erişilebilir değil");
        }

        return convertToDto(product);
    }

    public List<ProductDto> filterVisibleProducts(ProductCategory category, String city,
                                                  Double minPrice, Double maxPrice) {
        List<Product> products;

        if (category != null && city != null) {
            products = productRepository.findByCategoryAndStatusAndVisibleTrue(category, ProductStatus.ACTIVE)
                    .stream()
                    .filter(p -> p.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        } else if (category != null) {
            products = productRepository.findByCategoryAndStatusAndVisibleTrue(category, ProductStatus.ACTIVE);
        } else if (city != null) {
            products = productRepository.findByCityAndStatusAndVisibleTrue(city, ProductStatus.ACTIVE);
        } else {
            products = productRepository.findByStatusAndVisibleTrue(ProductStatus.ACTIVE);
        }

        if (minPrice != null && maxPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Admin endpoints - tüm ürünleri göster
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
        return convertToDto(product);
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToEntity(productDto);
        product.setStatus(ProductStatus.ACTIVE);
        if (productDto.getVisible() != null) {
            product.setVisible(productDto.getVisible());
        }
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
    }

    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));

        product.setTitle(productDto.getTitle());
        product.setCity(productDto.getCity());
        product.setCategory(productDto.getCategory());
        product.setStaff(productDto.getStaff());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setContactInfo(productDto.getContactInfo());

        if (productDto.getStatus() != null) {
            product.setStatus(productDto.getStatus());
        }

        if (productDto.getVisible() != null) {
            product.setVisible(productDto.getVisible());
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
        productRepository.delete(product);
    }

    @Transactional
    public void markAsSold(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
        product.setStatus(ProductStatus.SOLD);
        productRepository.save(product);
    }

    @Transactional
    public void toggleVisibility(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı: " + id));
        product.setVisible(!product.getVisible());
        productRepository.save(product);
    }

    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setTitle(product.getTitle());
        dto.setCity(product.getCity());
        dto.setCategory(product.getCategory());
        dto.setStaff(product.getStaff());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());
        dto.setContactInfo(product.getContactInfo());
        dto.setStatus(product.getStatus());
        dto.setVisible(product.getVisible());
        return dto;
    }

    private Product convertToEntity(ProductDto dto) {
        Product product = new Product();
        product.setTitle(dto.getTitle());
        product.setCity(dto.getCity());
        product.setCategory(dto.getCategory());
        product.setStaff(dto.getStaff());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());
        product.setContactInfo(dto.getContactInfo());
        return product;
    }
}
