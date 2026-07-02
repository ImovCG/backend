package com.imovcg.back.service;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imovcg.back.dto.ImoveisFiltrosDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.dto.ImovelPostDTO;
import com.imovcg.back.model.Imovel;
import com.imovcg.back.repository.ImovelRepository;
import com.imovcg.back.specification.ImovelSpecification;

import java.util.Optional;

@Service
@Transactional
public class ImovelService {
    
    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ImovelGetDTO saveImovel(ImovelPostDTO postDTO) {
        Imovel imovel;

        if (postDTO.getExternalId() != null && !postDTO.getExternalId().isBlank()) {
            Optional<Imovel> existing = imovelRepository.findByExternalId(postDTO.getExternalId());
            if (existing.isPresent()) {
                imovel = existing.get();
                modelMapper.map(postDTO, imovel); // atualiza campos existentes
                return modelMapper.map(imovelRepository.save(imovel), ImovelGetDTO.class);
            }
        }

        imovel = modelMapper.map(postDTO, Imovel.class);
        return modelMapper.map(imovelRepository.save(imovel), ImovelGetDTO.class);
    }

    public ImovelGetDTO getImovel(Long id) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com o ID: " + id));

        return modelMapper.map(imovel, ImovelGetDTO.class);
    }

    public Page<ImovelGetDTO> getImoveis (ImoveisFiltrosDTO filtrosDTO, Pageable pageable) {
        Specification<Imovel> spec = ImovelSpecification.filtros(filtrosDTO);

        return imovelRepository.findAll(spec, pageable).map(ImovelGetDTO::new);
    } 
}
