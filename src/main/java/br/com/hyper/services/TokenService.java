package br.com.hyper.services;

import br.com.hyper.entities.CustomerEntity;
import jakarta.servlet.http.Cookie;

import java.util.Optional;

public interface TokenService {

    Cookie generateAccessTokenCookie(CustomerEntity customer);

    Cookie generateRefreshTokenCookie(CustomerEntity customer);

    Optional<String> extractRefreshTokenFromCookies(Cookie[] cookies);
}
