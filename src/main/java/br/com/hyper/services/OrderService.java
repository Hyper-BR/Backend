package br.com.hyper.services;

import br.com.hyper.dtos.requests.OrderRequestDTO;
import br.com.hyper.dtos.responses.OrderResponseDTO;
import br.com.hyper.dtos.responses.pages.OrderPageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    public OrderResponseDTO save(OrderRequestDTO order);

    public OrderPageResponseDTO find(List<String> names, Pageable pageable);

    public OrderResponseDTO update(Long id, OrderRequestDTO order);
    public void delete(Long id);
}
