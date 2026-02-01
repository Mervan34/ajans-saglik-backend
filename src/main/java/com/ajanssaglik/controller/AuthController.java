package com.ajanssaglik.controller;

import com.ajanssaglik.dto.LoginRequest;
import com.ajanssaglik.dto.LoginResponse;
import com.ajanssaglik.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        try {

            // 1️⃣ Authentication yap
            Authentication authentication = authService.authenticate(request);

            // 2️⃣ SecurityContext'e authentication'ı koy
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3️⃣ Session oluştur ve SecurityContext bağla
            HttpSession session = httpRequest.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // 4️⃣ Cevap oluştur
            LoginResponse response = new LoginResponse(authentication.getName(), "Giriş başarılı");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null, "Kullanıcı adı veya şifre hatalı"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        System.out.println("📨 Logout request received");

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkAuth(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean authenticated = authService.isAuthenticated();


        return ResponseEntity.ok(authenticated);
    }
}