package com.ajanssaglik.service;

import com.ajanssaglik.model.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${admin.email}")
    private String adminEmail;

    @Async
    public void sendContactRequestEmail(ContactRequest request) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("Yeni İletişim Talebi - " + request.getRequestType().getDisplayName());

            StringBuilder text = new StringBuilder();
            text.append("Yeni bir iletişim talebi alındı:\n\n");
            text.append("Ad Soyad: ").append(request.getName()).append("\n");
            text.append("Email: ").append(request.getEmail()).append("\n");
            text.append("Telefon: ").append(request.getPhone()).append("\n");
            text.append("Talep Tipi: ").append(request.getRequestType().getDisplayName()).append("\n");
            text.append("Ürün ").append(request.getRequestType()).append("\n");
            if (request.getProductId() != null) {
                text.append("İlgilendiği Ürün: ").append(request.getProductTitle()).append("\n");
                text.append("Ürün ID: ").append(request.getProductTitle()).append("\n");
            }

            text.append("\nMesaj:\n").append(request.getMessage());

            message.setText(text.toString());
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Email gönderilemedi: " + e.getMessage());
        }
    }
}
