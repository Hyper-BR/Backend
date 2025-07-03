package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.LoginResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest, HttpServletResponse http) {

        LoginResponseDTO response = loginService.login(loginRequest, http);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@AuthenticationPrincipal CustomerEntity customer) {

        LoginResponseDTO response = loginService.refresh(customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
