package br.com.hyper.services;

import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtUtil jwtUtil;

    public Cookie generateAccessTokenCookie(CustomerEntity customer) {
        String token = jwtUtil.generateAccessToken(customer.getId().toString());
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
        return cookie;
    }

    public Cookie generateRefreshTokenCookie(CustomerEntity customer) {
        String token = jwtUtil.generateRefreshToken(customer.getId().toString());
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }

    public Optional<String> extractRefreshTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) return Optional.empty();

        return Arrays.stream(cookies)
                .filter(c -> "refresh_token".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}