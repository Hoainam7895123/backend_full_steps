package dev.hoainamtd.service;

import dev.hoainamtd.util.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    // tạo ra token, gửi cho client
    String generateToken(UserDetails user);

    String generateRefreshToken(UserDetails user);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType type, UserDetails user);
}
