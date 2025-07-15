package br.com.hyper.configuration;

import br.com.hyper.dtos.responses.TrackResponseDTO;
import br.com.hyper.entities.TrackEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.typeMap(TrackEntity.class, TrackResponseDTO.class)
                .addMappings(m -> m.map(TrackEntity::getCover, TrackResponseDTO::setCoverUrl));

        return mapper;
    }
}
