package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.exceptions.CustomerException;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.utils.JwtUtil;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final CustomerRepository customerRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final ModelMapper modelMapper;

    private final TokenService tokenService;

    public CustomerResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse http) {
        UsernamePasswordAuthenticationToken login =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication auth = authenticationManager.authenticate(login);
        CustomerEntity customer = (CustomerEntity) auth.getPrincipal();

        Cookie accessCookie = tokenService.generateAccessTokenCookie(customer);
        Cookie refreshCookie = tokenService.generateRefreshTokenCookie(customer);

        http.addCookie(accessCookie);
        http.addCookie(refreshCookie);

        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

    @Override
    public CustomerResponseDTO me(@AuthenticationPrincipal CustomerEntity customer) {

        if (customer == null) {
            throw new CustomerException(ErrorCodes.UNAUTHORIZED, "User not authenticated");
        }

        CustomerEntity customerEntity = findByEmailOrThrowUserDataNotFoundException(customer.getEmail());

        return modelMapper.map(customerEntity, CustomerResponseDTO.class);
    }

    @Override
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        tokenService.extractRefreshTokenFromCookies(request.getCookies())
                .ifPresentOrElse(token -> {
                    try {
                        String userId = jwtUtil.validateToken(token);
                        Optional<CustomerEntity> customerOpt = customerRepository.findById(UUID.fromString(userId));

                        if (customerOpt.isEmpty()) {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            return;
                        }

                        Cookie newAccess = tokenService.generateAccessTokenCookie(customerOpt.get());
                        response.addHeader(HttpHeaders.SET_COOKIE, newAccess.toString());

                    } catch (TokenExpiredException e) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    } catch (Exception e) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }, () -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                });
    }

    @Override
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    private CustomerEntity findByEmailOrThrowUserDataNotFoundException(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomerException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
