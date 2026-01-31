package com.ajanssaglik.dto;

import com.ajanssaglik.model.ProductCategory;
import com.ajanssaglik.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;

    @NotBlank(message = "Başlık zorunludur")
    private String title;

    @NotBlank(message = "Şehir zorunludur")
    private String city;

    @NotNull(message = "Kategori zorunludur")
    private ProductCategory category;

    private String staff;

    @NotNull(message = "Fiyat zorunludur")
    @Positive(message = "Fiyat pozitif olmalıdır")
    private Double price;

    private String description;
    private String contactInfo;
    private ProductStatus status;

    @NotNull(message = "Görünürlük durumu zorunludur")
    private Boolean visible;
}