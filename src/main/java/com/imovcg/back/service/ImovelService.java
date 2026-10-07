package com.imovcg.back.service;


import java.util.ArrayList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imovcg.back.completude.CalculadoraCompletudeImovel;
import com.imovcg.back.dto.ImoveisFiltrosDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.dto.ImovelPostDTO;
import com.imovcg.back.model.ImovelFoto;
import com.imovcg.back.model.Imovel;
import com.imovcg.back.repository.ImovelRepository;
import com.imovcg.back.specification.ImovelSpecification;
import com.imovcg.back.util.ImovelHash;
import com.imovcg.back.util.UniversidadeCampinaGrande;

import java.util.Optional;

@Service
@Transactional
public class ImovelService {

    private final ImovelRepository imovelRepository;
    private final ModelMapper modelMapper;

    public ImovelService(ImovelRepository imovelRepository, ModelMapper modelMapper) {
        this.imovelRepository = imovelRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.typeMap(ImovelPostDTO.class, Imovel.class)
                .addMappings(mapper -> mapper.skip(Imovel::setFotos));
    }

    public ImovelGetDTO saveImovel(ImovelPostDTO postDTO) {
        Imovel imovel;

        if (postDTO.getFonte() != null && !postDTO.getFonte().isBlank()
                && postDTO.getExternalId() != null && !postDTO.getExternalId().isBlank()) {
            Optional<Imovel> existing = imovelRepository.findByFonteAndExternalId(
                    postDTO.getFonte(),
                    postDTO.getExternalId());
            if (existing.isPresent()) {
                imovel = existing.get();
                modelMapper.map(postDTO, imovel);
                sincronizarFotos(imovel, postDTO.getFotos());
                imovel.setHash(ImovelHash.gerarHash(postDTO));
                atualizarCompletude(imovel);
                imovelRepository.save(imovel);
                return new ImovelGetDTO(imovel);
            }
        }

        if (postDTO.getExternalId() != null && !postDTO.getExternalId().isBlank()) {
            Optional<Imovel> existing = imovelRepository.findByExternalId(postDTO.getExternalId());
            if (existing.isPresent()) {
                imovel = existing.get();
                modelMapper.map(postDTO, imovel);
                sincronizarFotos(imovel, postDTO.getFotos());
                imovel.setHash(ImovelHash.gerarHash(postDTO));
                atualizarCompletude(imovel);
                imovelRepository.save(imovel);
                return new ImovelGetDTO(imovel);
            }
        }

        String hash = ImovelHash.gerarHash(postDTO);
        Optional<Imovel> existingByHash = imovelRepository.findByHash(hash);
        if (existingByHash.isPresent()) {
            imovel = existingByHash.get();
            atualizarCompletude(imovel);
            imovelRepository.save(imovel);
            return new ImovelGetDTO(imovel);
        }

        imovel = modelMapper.map(postDTO, Imovel.class);
        imovel.setHash(hash);
        sincronizarFotos(imovel, postDTO.getFotos());
        atualizarCompletude(imovel);
        imovelRepository.save(imovel);
        return new ImovelGetDTO(imovel);
    }

    public List<ImovelGetDTO> saveLote(List<ImovelPostDTO> dtos) {
        List<ImovelGetDTO> resultado = new ArrayList<>();

        for (ImovelPostDTO dto : dtos) {
            resultado.add(saveImovel(dto));
        }

        return resultado;
    }

    public ImovelGetDTO getImovel(Long id) {
        Imovel imovel = imovelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Imóvel não encontrado com o ID: " + id));

        return new ImovelGetDTO(imovel);
    }

    public Page<ImovelGetDTO> getImoveis (ImoveisFiltrosDTO filtrosDTO, Pageable pageable) {
        Specification<Imovel> spec = ImovelSpecification.filtros(filtrosDTO);
        UniversidadeCampinaGrande universidade = filtrosDTO.getUniversidade() == null
                || filtrosDTO.getUniversidade().isBlank()
                ? null : UniversidadeCampinaGrande.from(filtrosDTO.getUniversidade());

        return imovelRepository.findAll(spec, pageable)
                .map(imovel -> new ImovelGetDTO(imovel, universidade));
    } 

    private void atualizarCompletude(Imovel imovel) {
        int percentual = CalculadoraCompletudeImovel.avaliar(imovel).percentual();
        imovel.setCompletude(percentual);
    }

    private void sincronizarFotos(Imovel imovel, List<String> fotos) {
        imovel.getFotos().clear();

        if (fotos == null || fotos.isEmpty()) {
            return;
        }

        int ordem = 0;
        for (String url : fotos) {
            if (url == null || url.isBlank()) {
                continue;
            }

            ImovelFoto foto = new ImovelFoto();
            foto.setImovel(imovel);
            foto.setUrl(url);
            foto.setOrdem(ordem++);
            imovel.getFotos().add(foto);
        }
    }
}
