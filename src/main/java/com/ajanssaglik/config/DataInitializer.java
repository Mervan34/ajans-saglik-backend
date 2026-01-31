package com.ajanssaglik.config;

import com.ajanssaglik.model.User;
import com.ajanssaglik.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Admin kullanıcısı yoksa oluştur
        if (!userRepository.existsByUsername("adminZine")) {
            User admin = new User();

            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

        }
    }
}
