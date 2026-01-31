package com.ajanssaglik.dto;

import com.ajanssaglik.model.RequestType;
import com.ajanssaglik.model.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequestDto {
    private Long id;

    @NotBlank(message = "İsim zorunludur")
    private String name;

    @NotBlank(message = "Email zorunludur")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Telefon zorunludur")
    private String phone;

    @NotNull(message = "Talep tipi zorunludur")
    private RequestType requestType;

    @NotBlank(message = "Mesaj zorunludur")
    private String message;

    private Long productId;
    private String productTitle;
    private RequestStatus status;
    private LocalDateTime createdAt;
}