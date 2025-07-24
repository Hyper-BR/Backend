package br.com.hyper.controllers;

import br.com.hyper.dtos.InsightsDTO;
import br.com.hyper.entities.CustomerEntity;
import br.com.hyper.services.InsightsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InsightsController {

    private final InsightsService insightsService;

    @GetMapping(value = "/insights")
    public ResponseEntity<InsightsDTO> getInsights(@AuthenticationPrincipal CustomerEntity customer) {

        InsightsDTO response = insightsService.getInsights(customer);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
