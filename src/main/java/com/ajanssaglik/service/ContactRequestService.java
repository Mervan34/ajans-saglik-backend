package com.ajanssaglik.service;

import com.ajanssaglik.dto.ContactRequestDto;
import com.ajanssaglik.model.ContactRequest;
import com.ajanssaglik.model.Product;
import com.ajanssaglik.model.RequestStatus;
import com.ajanssaglik.repository.ContactRequestRepository;
import com.ajanssaglik.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactRequestService {
    private final ContactRequestRepository contactRequestRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;

    @Transactional
    public ContactRequestDto createContactRequest(ContactRequestDto dto) {
        ContactRequest request = new ContactRequest();
        request.setName(dto.getName());
        request.setEmail(dto.getEmail());
        request.setPhone(dto.getPhone());
        request.setRequestType(dto.getRequestType());
        request.setMessage(dto.getMessage());
        request.setProductId(dto.getProductId());

        if (dto.getProductId() != null) {
            Product product = productRepository.findById(dto.getProductId()).orElse(null);
            if (product != null) {
                request.setProductTitle(product.getTitle());
            }
        }

        request.setStatus(RequestStatus.PENDING);
        ContactRequest savedRequest = contactRequestRepository.save(request);

        // Admin'e email gönder
        emailService.sendContactRequestEmail(savedRequest);

        return convertToDto(savedRequest);
    }

    public List<ContactRequestDto> getAllContactRequests() {
        return contactRequestRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ContactRequestDto getContactRequestById(Long id) {
        ContactRequest request = contactRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı: " + id));
        return convertToDto(request);
    }
    public void delete(Long id) {
        contactRequestRepository.deleteById(id);
    }

    @Transactional
    public void updateRequestStatus(Long id, RequestStatus status) {
        ContactRequest request = contactRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı: " + id));
        request.setStatus(status);
        contactRequestRepository.save(request);
    }

    private ContactRequestDto convertToDto(ContactRequest request) {
        ContactRequestDto dto = new ContactRequestDto();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setPhone(request.getPhone());
        dto.setRequestType(request.getRequestType());
        dto.setMessage(request.getMessage());
        dto.setProductId(request.getProductId());
        dto.setProductTitle(request.getProductTitle());
        dto.setStatus(request.getStatus());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}
