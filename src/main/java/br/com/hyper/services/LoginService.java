package br.com.hyper.services;

import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

    CustomerResponseDTO login(LoginRequestDTO authentication, HttpServletResponse http);

    CustomerResponseDTO me(CustomerEntity customer);

    void logout(HttpServletResponse response);

}
