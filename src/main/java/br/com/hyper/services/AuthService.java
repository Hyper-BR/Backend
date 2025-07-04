package br.com.hyper.services;

import br.com.hyper.dtos.requests.AuthRequestDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    CustomerResponseDTO register(CustomerRequestDTO customer, HttpServletResponse http);

    CustomerResponseDTO login(AuthRequestDTO authentication, HttpServletResponse http);

    CustomerResponseDTO me(CustomerEntity customer);

    void refresh(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletResponse response);

}
