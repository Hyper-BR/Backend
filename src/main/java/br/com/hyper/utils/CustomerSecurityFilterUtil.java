package br.com.hyper.utils;

import br.com.hyper.repositories.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomerSecurityFilterUtil extends OncePerRequestFilter {

    @Autowired
    private CustomerTokenUtil customerTokenUtil;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if(token != null) {
            String email = customerTokenUtil.validateToken(token);
            UserDetails user = customerRepository.findByEmailUserDetails(email);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String authHeader = request.getHeader("Authorization");

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                logger.info(cookie);
                if (cookie.getName().equals("token")) return cookie.getValue();
            }
        } else if(authHeader != null) {
            return authHeader.replace("Bearer ", "");
        }
        return null;

    }
}
