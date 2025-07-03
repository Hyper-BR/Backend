package br.com.hyper.utils;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class MapperUtils {

    /**
     * Mapeia uma Page<S> para Page<T> usando o ModelMapper.
     *
     * @param source      página original com entidades
     * @param targetClass classe DTO de destino
     * @param modelMapper instância do ModelMapper
     * @param <S>         tipo da entidade de origem
     * @param <T>         tipo da entidade de destino
     * @return Page<T> mapeada com o mesmo pageable da origem
     */
    public static <S, T> Page<T> mapPage(Page<S> source, Class<T> targetClass, ModelMapper modelMapper) {
        List<T> content = source.getContent().stream()
                .map(entity -> modelMapper.map(entity, targetClass))
                .collect(Collectors.toList());

        return new PageImpl<>(content, source.getPageable(), source.getTotalElements());
    }
}
