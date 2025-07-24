package br.com.hyper.services;

import br.com.hyper.dtos.InsightsDTO;
import br.com.hyper.entities.CustomerEntity;

public interface InsightsService {

    InsightsDTO getInsights(CustomerEntity customer);
}
