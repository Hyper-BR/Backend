package br.com.hyper.services;

import br.com.hyper.constants.ErrorCodes;
import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.dtos.responses.LoginResponseDTO;
import br.com.hyper.dtos.responses.TokenResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.entities.SubscriptionEntity;
import br.com.hyper.enums.UserRole;
import br.com.hyper.exceptions.CustomerException;
import br.com.hyper.repositories.CustomerRepository;
import br.com.hyper.repositories.SubscriptionRepository;
import br.com.hyper.utils.CustomerTokenUtil;
import br.com.hyper.utils.PaginationMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final CustomerRepository customerRepository;

    private final AuthenticationManager authenticationManager;

    private final CustomerTokenUtil customerTokenUtil;

    private final ModelMapper modelMapper;

    private final PaginationMapper paginationMapper;

    public LoginResponseDTO login(LoginRequestDTO loginRequest, HttpServletResponse http) {

        UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication auth = authenticationManager.authenticate(login);

        String token = customerTokenUtil.generateToken((CustomerEntity) auth.getPrincipal());

        TokenResponseDTO tokenResponse = new TokenResponseDTO();
        tokenResponse.setToken(token);

        CustomerEntity customer = findByEmailOrThrowUserDataNotFoundException(loginRequest.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(tokenResponse);
        response.setCustomer(modelMapper.map(customer, CustomerResponseDTO.class));

        modelMapper.map(customer, response);

        return response;
    }

    public LoginResponseDTO refresh(CustomerEntity customer) {

        CustomerEntity customerEntity = findByEmailOrThrowUserDataNotFoundException(customer.getEmail());
        String token = customerTokenUtil.generateToken(customerEntity);

        TokenResponseDTO tokenResponse = new TokenResponseDTO();
        tokenResponse.setToken(token);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(tokenResponse);
        response.setCustomer(modelMapper.map(customerEntity, CustomerResponseDTO.class));

        return response;
    }



    private CustomerEntity findByEmailOrThrowUserDataNotFoundException(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new CustomerException(ErrorCodes.DATA_NOT_FOUND, ErrorCodes.DATA_NOT_FOUND.getMessage()));
    }

}
