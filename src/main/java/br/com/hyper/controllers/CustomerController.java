package br.com.hyper.controllers;

import br.com.hyper.dtos.requests.LoginRequestDTO;
import br.com.hyper.dtos.responses.LoginResponseDTO;
import br.com.hyper.dtos.responses.pages.CustomerPageResponseDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.CustomerService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private final CustomerService customerService;

    @PostMapping(value = "/customer")
    public ResponseEntity<CustomerResponseDTO> save(@RequestBody @Valid CustomerRequestDTO customer){

        CustomerResponseDTO response = customerService.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/customer/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest, HttpServletResponse http) {

        LoginResponseDTO response = customerService.login(loginRequest, http);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/customer/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@AuthenticationPrincipal CustomerEntity customer) {

        LoginResponseDTO response = customerService.refresh(customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/customer/{email}")
    public ResponseEntity<CustomerResponseDTO> findByEmail(@PathVariable String email) {

        CustomerResponseDTO response = customerService.findByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/customer")
    public ResponseEntity<CustomerPageResponseDTO> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "sort", defaultValue = "UNSORT", required = false) String sort,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        CustomerPageResponseDTO response = customerService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/customer/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable Long id, @RequestBody @Valid CustomerRequestDTO user) {

        CustomerResponseDTO response = customerService.update(id, user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/customer/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        customerService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
