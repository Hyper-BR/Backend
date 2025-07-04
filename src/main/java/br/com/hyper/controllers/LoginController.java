package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<CustomerResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest, HttpServletResponse http) {

        CustomerResponseDTO response = loginService.login(loginRequest, http);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<CustomerResponseDTO> me(@AuthenticationPrincipal CustomerEntity customer) {

        CustomerResponseDTO response = loginService.me(customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        loginService.logout(response);

        return ResponseEntity.noContent().build();
    }
}
