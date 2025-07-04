package br.com.hyper.utils;

import br.com.hyper.repositories.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class JwtAuthenticationFilterUtil extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    public JwtAuthenticationFilterUtil(JwtUtil jwtUtil, CustomerRepository customerRepository) {
        this.jwtUtil = jwtUtil;
        this.customerRepository = customerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getAccessTokenFromRequest(request);

            if (token != null) {
                try {
                    String userId = jwtUtil.validateToken(token);
                    UserDetails user = customerRepository.findByIdUserDetails(UUID.fromString(userId));

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (Exception e) {
                    log.error("Erro ao validar token JWT: {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
