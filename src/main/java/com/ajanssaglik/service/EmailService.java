package com.ajanssaglik.service;

import com.ajanssaglik.model.ContactRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${admin.email}")
    private String adminEmail;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendContactRequestEmail(ContactRequest request) {

        // Resend: domain doğrulamadıysan bu from ile başla
        String from = "onboarding@resend.dev";

        String subject = "Yeni İletişim Talebi - " + request.getRequestType().getDisplayName();

        String html = buildHtml(request);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(resendApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> payload = Map.of(
                "from", from,
                "to", adminEmail,
                "subject", subject,
                "html", html
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.resend.com/emails",
                    entity,
                    String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Resend mail gönderimi başarısız. Status=" +
                        response.getStatusCode() + " Body=" + response.getBody());
            }

            System.out.println("✅ Resend ile mail gönderildi. Response: " + response.getBody());

        } catch (Exception e) {
            // Burada swallow etme: log bas ki Render’da görebilesin
            System.err.println("❌ Resend mail gönderilemedi: " + e.getMessage());
            throw new RuntimeException("Mail gönderilemedi (Resend)", e);
        }
    }

    private String buildHtml(ContactRequest request) {
        String productInfo = "";
        if (request.getProductId() != null) {
            productInfo = """
                    <p><b>İlgilendiği Ürün:</b> %s</p>
                    <p><b>Ürün ID:</b> %s</p>
                    """.formatted(
                    safe(request.getProductTitle()),
                    safe(String.valueOf(request.getProductId()))
            );
        }

        return """
                <h2>Yeni bir iletişim talebi alındı</h2>
                <p><b>Ad Soyad:</b> %s</p>
                <p><b>Email:</b> %s</p>
                <p><b>Telefon:</b> %s</p>
                <p><b>Talep Tipi:</b> %s</p>
                %s
                <hr/>
                <p><b>Mesaj:</b></p>
                <p>%s</p>
                """.formatted(
                safe(request.getName()),
                safe(request.getEmail()),
                safe(request.getPhone()),
                safe(request.getRequestType().getDisplayName()),
                productInfo,
                safe(request.getMessage()).replace("\n", "<br/>")
        );
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
