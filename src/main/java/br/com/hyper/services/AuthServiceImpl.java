package br.com.hyper.services;

import br.com.hyper.constants.BaseUrls;
import br.com.hyper.entities.CartEntity;
import br.com.hyper.enums.ErrorCodes;
import br.com.hyper.dtos.requests.AuthRequestDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.enums.UserRole;
import br.com.hyper.exceptions.GenericException;
import br.com.hyper.repositories.CartRepository;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.repositories.SubscriptionRepository;
import br.com.hyper.utils.JwtUtil;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final AuthenticationManager authenticationManager;
    private final SubscriptionRepository subscriptionRepository;
    private final CartRepository cartRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final TokenService tokenService;

    @Override
    public CustomerResponseDTO register(CustomerRequestDTO customer, HttpServletResponse http) {
        CustomerEntity customerEntity;
        try {
            SubscriptionEntity subscription = subscriptionRepository.findById(1L).orElseThrow(() -> new EntityNotFoundException("Subscription not found"));

            if (customerRepository.findByEmail(customer.getEmail()).isPresent()){
                throw new GenericException(ErrorCodes.DUPLICATED_DATA, ErrorCodes.DUPLICATED_DATA.getMessage());
            }

            customerEntity = modelMapper.map(customer, CustomerEntity.class);

            customerEntity.setRole(UserRole.CUSTOMER);
            customerEntity.setSubscription(subscription);
            customerEntity.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
            customerEntity.setIsArtist(false);
            customerEntity.setIsLabel(false);
            customerEntity.setPlaylists(new ArrayList<>());
            customerEntity.setAvatarUrl(BaseUrls.AVATAR_URL);

            CartEntity cart = new CartEntity();
            cart.setName("Principal");
            cart.setCustomer(customerEntity);

            customerEntity.getCarts().add(cart);
            customerEntity = customerRepository.save(customerEntity);

            cartRepository.save(cart);

            Cookie accessCookie = tokenService.generateAccessTokenCookie(customerEntity);
            Cookie refreshCookie = tokenService.generateRefreshTokenCookie(customerEntity);

            http.addCookie(accessCookie);
            http.addCookie(refreshCookie);

            return modelMapper.map(customerEntity, CustomerResponseDTO.class);
        }  catch (DataIntegrityViolationException e) {
            throw new GenericException(ErrorCodes.DUPLICATED_DATA,
                    ErrorCodes.DUPLICATED_DATA.getMessage());
        }
    }

    public CustomerResponseDTO login(AuthRequestDTO loginRequest, HttpServletResponse http) {
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
            throw new GenericException(ErrorCodes.UNAUTHORIZED, "User not authenticated");
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
        Cookie accessToken = new Cookie("access_token", null);
        accessToken.setPath("/");
        accessToken.setMaxAge(0);
        accessToken.setHttpOnly(true);
        accessToken.setSecure(true);
        response.addCookie(accessToken);

        Cookie refreshToken = new Cookie("refresh_token", null);
        refreshToken.setPath("/");
        refreshToken.setMaxAge(0);
        refreshToken.setHttpOnly(true);
        refreshToken.setSecure(true);
        response.addCookie(refreshToken);
    }


    private CustomerEntity findByEmailOrThrowUserDataNotFoundException(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new GenericException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
