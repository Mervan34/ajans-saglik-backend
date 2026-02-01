package com.ajanssaglik.controller;

import com.ajanssaglik.dto.ContactRequestDto;
import com.ajanssaglik.service.ContactRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {
    private final ContactRequestService contactRequestService;

    @PostMapping
    public ResponseEntity<ContactRequestDto> createContactRequest(
            @Valid @RequestBody ContactRequestDto request) {
        try {
            ContactRequestDto savedRequest = contactRequestService.createContactRequest(request);
            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

