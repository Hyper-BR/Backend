package br.com.hyper.services;

import br.com.hyper.dtos.InsightsDTO;
import br.com.hyper.entities.CustomerEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsightsServiceImpl implements InsightsService {

    @Override
    public InsightsDTO getInsights(CustomerEntity customer) {
        return null;
    }
}
