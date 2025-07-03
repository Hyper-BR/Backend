package br.com.hyper.services;

import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.LoginResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {

    LoginResponseDTO login(LoginRequestDTO authentication, HttpServletResponse http);

    LoginResponseDTO refresh(CustomerEntity customer);

}
