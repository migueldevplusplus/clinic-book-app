package com.clinicbook.domain.port;

import com.clinicbook.domain.model.User;

public interface JwtTokenProviderPort {
    String generateToken(User user);
    String extractEmail(String token);
    boolean isTokenValid(String token);
}