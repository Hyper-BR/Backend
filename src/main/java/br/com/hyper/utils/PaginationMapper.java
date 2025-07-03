package br.com.hyper.utils;

import br.com.hyper.dtos.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaginationMapper {

    private static ModelMapper modelMapper;

    public static <S, T> PageResponseDTO<T> map(Page<S> page, Class<T> targetClass) {
        List<T> content = page.getContent().stream()
                .map(source -> modelMapper.map(source, targetClass))
                .collect(Collectors.toList());

        return PageResponseDTO.<T>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }
}

