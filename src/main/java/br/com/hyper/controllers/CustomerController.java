package br.com.hyper.controllers;

import br.com.hyper.dtos.PageResponseDTO;
import br.com.hyper.dtos.requests.CustomerRequestDTO;
import br.com.hyper.dtos.responses.CustomerResponseDTO;
import br.com.hyper.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping(value = "/customer")
    public ResponseEntity<CustomerResponseDTO> save(@RequestBody @Valid CustomerRequestDTO customer){

        CustomerResponseDTO response = customerService.save(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(value = "/customer/{email}")
    public ResponseEntity<CustomerResponseDTO> findByEmail(@PathVariable String email) {

        CustomerResponseDTO response = customerService.findByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(value = "/customer")
    public ResponseEntity<PageResponseDTO<CustomerResponseDTO>> find(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {

        Pageable pageable = PageRequest.of(page, size);

        PageResponseDTO<CustomerResponseDTO> response = customerService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(value = "/customer/{id}")
    public ResponseEntity<CustomerResponseDTO> update(@PathVariable UUID id, @RequestBody CustomerRequestDTO user) {

        CustomerResponseDTO response = customerService.update(id, user);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(value = "/customer/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){

        customerService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
