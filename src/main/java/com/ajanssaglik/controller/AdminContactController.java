package com.ajanssaglik.controller;

import com.ajanssaglik.dto.ContactRequestDto;
import com.ajanssaglik.model.RequestStatus;
import com.ajanssaglik.service.ContactRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/contacts")
@RequiredArgsConstructor
public class AdminContactController {
    private final ContactRequestService contactRequestService;

    @GetMapping
    public ResponseEntity<List<ContactRequestDto>> getAllContactRequests() {
        return ResponseEntity.ok(contactRequestService.getAllContactRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactRequestDto> getContactRequest(@PathVariable Long id) {
        try {
            ContactRequestDto request = contactRequestService.getContactRequestById(id);
            return ResponseEntity.ok(request);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam RequestStatus status) {
        try {
            contactRequestService.updateRequestStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForce(@PathVariable Long id) {
        contactRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
