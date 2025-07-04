package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.exceptions.CustomerException;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.utils.CustomerTokenUtil;
import br.com.hyper.utils.PaginationMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final CustomerRepository customerRepository;

    private final AuthenticationManager authenticationManager;

    private final CustomerTokenUtil customerTokenUtil;

    private final ModelMapper modelMapper;

    public CustomerResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse http) {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication auth = authenticationManager.authenticate(login);

        String token = customerTokenUtil.generateToken((CustomerEntity) auth.getPrincipal());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(2 * 24 * 60 * 60);
        http.addCookie(cookie);

        CustomerEntity customer = findByEmailOrThrowUserDataNotFoundException(loginRequest.getEmail());

        return modelMapper.map(customer, CustomerResponseDTO.class);
    }

    @Override
    public CustomerResponseDTO me(CustomerEntity customer) {

        if (customer == null) {
            throw new CustomerException(ErrorCodes.UNAUTHORIZED, "User not authenticated");
        }

        CustomerEntity customerEntity = findByEmailOrThrowUserDataNotFoundException(customer.getEmail());

        return modelMapper.map(customerEntity, CustomerResponseDTO.class);
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
