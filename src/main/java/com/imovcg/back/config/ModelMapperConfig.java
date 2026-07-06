package com.imovcg.back.config;

import com.imovcg.back.dto.ImovelPostDTO;
import com.imovcg.back.model.Imovel;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.typeMap(ImovelPostDTO.class, Imovel.class).addMappings(mapper -> {
            mapper.skip(Imovel::setId);
            mapper.skip(Imovel::setHash);
        });

        return modelMapper;
    }
}