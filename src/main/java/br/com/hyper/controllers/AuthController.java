package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.AuthRequestDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/auth/register")
    public ResponseEntity<CustomerResponseDTO> register(@RequestBody @Valid CustomerRequestDTO customer, HttpServletResponse http){

        CustomerResponseDTO response = authService.register(customer, http);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/auth/login")
    public ResponseEntity<CustomerResponseDTO> login(@RequestBody @Valid AuthRequestDTO loginRequest, HttpServletResponse http) {

        CustomerResponseDTO response = authService.login(loginRequest, http);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<CustomerResponseDTO> me(@AuthenticationPrincipal CustomerEntity customer) {

        CustomerResponseDTO response = authService.me(customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {

        authService.logout(response);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        authService.refresh(request, response);
        return ResponseEntity.noContent().build();
    }

}
