package com.imovcg.back.controller;

import com.imovcg.back.dto.ImoveisFiltrosDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.dto.ImovelLoteDTO;
import com.imovcg.back.dto.ImovelPostDTO;
import com.imovcg.back.service.ImovelService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/imoveis")
public class ImovelController {

    @Autowired
    private ImovelService imovelService;

    @GetMapping
    public ResponseEntity<Page<ImovelGetDTO>> getImoveis(
            @Valid ImoveisFiltrosDTO filtros, Pageable pageable) {
        if (filtros.getDistanciaMaximaKm() != null
                && (filtros.getUniversidade() == null || filtros.getUniversidade().isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "universidade é obrigatória quando distanciaMaximaKm for informada");
        }
        return ResponseEntity.ok(imovelService.getImoveis(
                filtros, aplicarOrdenacaoPadrao(pageable)));
    }

    @GetMapping("/fonte/{fonte}")
    public ResponseEntity<Page<ImovelGetDTO>> getImoveisPorFonte(@PathVariable String fonte, Pageable pageable) {
        ImoveisFiltrosDTO filtros = new ImoveisFiltrosDTO();
        filtros.setFonte(fonte);
        return ResponseEntity.ok(imovelService.getImoveis(filtros, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelGetDTO> getImovel(@PathVariable Long id) {
        return ResponseEntity.ok(imovelService.getImovel(id));
    }
    
    @PostMapping
    public ResponseEntity<ImovelGetDTO> createImovel(@RequestBody @Valid ImovelPostDTO postDTO) {
        ImovelGetDTO created = imovelService.saveImovel(postDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/lote")
    public ResponseEntity<List<ImovelGetDTO>> createLote(
            @RequestBody @Valid ImovelLoteDTO loteDTO) {
        return ResponseEntity.status(201).body(imovelService.saveLote(loteDTO.getImoveis()));
    }

    private Pageable aplicarOrdenacaoPadrao(Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            return pageable;
        }

        Sort ordenacao = Sort.by(
                Sort.Order.desc("completude").nullsLast(),
                Sort.Order.desc("updatedAt"),
                Sort.Order.desc("id"));
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), ordenacao);
    }
}
