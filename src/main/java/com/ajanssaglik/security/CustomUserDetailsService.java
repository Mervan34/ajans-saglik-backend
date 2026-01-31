package com.ajanssaglik.security;

import com.ajanssaglik.model.User;
import com.ajanssaglik.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {

                    return new UsernameNotFoundException("Kullanıcı bulunamadı: " + username);
                });

        // Role kontrolü
        String role = user.getRole();
        if (role == null || role.isEmpty()) {

            role = "USER"; // default role
        }

        // ROLE_ prefix ekle
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}