package com.imovcg.back.controller;

import com.imovcg.back.dto.AnuncioDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.service.AnuncioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Painel do anunciante. Todas as rotas exigem JWT (ver SecurityConfig).
 */
@RestController
@RequestMapping("/api/anunciante/imoveis")
public class AnuncianteImovelController {

    private final AnuncioService anuncioService;

    public AnuncianteImovelController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @GetMapping
    public ResponseEntity<List<ImovelGetDTO>> listarMeus(
            @AuthenticationPrincipal Long anuncianteId) {
        return ResponseEntity.ok(anuncioService.listarMeus(anuncianteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelGetDTO> getMeu(
            @AuthenticationPrincipal Long anuncianteId, @PathVariable Long id) {
        return ResponseEntity.ok(anuncioService.getMeu(anuncianteId, id));
    }

    @PostMapping
    public ResponseEntity<ImovelGetDTO> criar(
            @AuthenticationPrincipal Long anuncianteId, @RequestBody @Valid AnuncioDTO dto) {
        return ResponseEntity.status(201).body(anuncioService.criar(anuncianteId, dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelGetDTO> atualizar(
            @AuthenticationPrincipal Long anuncianteId,
            @PathVariable Long id,
            @RequestBody @Valid AnuncioDTO dto) {
        return ResponseEntity.ok(anuncioService.atualizar(anuncianteId, id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @AuthenticationPrincipal Long anuncianteId, @PathVariable Long id) {
        anuncioService.excluir(anuncianteId, id);
        return ResponseEntity.noContent().build();
    }
}
