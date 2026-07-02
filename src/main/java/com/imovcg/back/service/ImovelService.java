package com.imovcg.back.service;

import java.util.UUID;
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

@Service
public class ImovelService {
    
    @Autowired
    private ImovelRepository imovelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ImovelEmailService imovelEmailService;

    @Transactional
    public ImovelGetDTO saveImovel(ImovelPostDTO postDTO) {
        Imovel imovel = modelMapper.map(postDTO, Imovel.class);

        imovel.setConfirmado(false);
        imovel.setVerificationToken(UUID.randomUUID().toString());

        Imovel savedImovel = imovelRepository.save(imovel);
        imovelEmailService.sendVerificationEmail(savedImovel);

        return modelMapper.map(savedImovel, ImovelGetDTO.class);
    }

    public ImovelGetDTO getImovel(Long id) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com o ID: " + id));

        return modelMapper.map(imovel, ImovelGetDTO.class);
    }

    @Transactional
    public ImovelGetDTO confirmImovel(String verificationToken) {
        Imovel imovel = imovelRepository.findByVerificationToken(verificationToken)
            .orElseThrow(() -> new RuntimeException("Token de confirmação inválido ou expirado."));

        imovel.setConfirmado(true);
        imovel.setVerificationToken(null);

        return modelMapper.map(imovelRepository.save(imovel), ImovelGetDTO.class);
    }

    public Page<ImovelGetDTO> getImoveis (ImoveisFiltrosDTO filtrosDTO, Pageable pageable) {
        Specification<Imovel> spec = ImovelSpecification.filtros(filtrosDTO);

        return imovelRepository.findAll(spec, pageable).map(ImovelGetDTO::new);
    } 
}
