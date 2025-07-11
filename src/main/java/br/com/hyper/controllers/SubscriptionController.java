package br.com.hyper.controllers;

import br.com.hyper.dtos.SubscriptionDTO;
import br.com.hyper.services.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping(value = "/subscriptions")
    public ResponseEntity<List<SubscriptionDTO>> findAll() {

        List<SubscriptionDTO> response = subscriptionService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
