package com.imovcg.back.controller;

import com.imovcg.back.dto.AnuncianteDTO;
import com.imovcg.back.dto.AnunciantePerfilDTO;
import com.imovcg.back.dto.AuthResponseDTO;
import com.imovcg.back.dto.GoogleLoginDTO;
import com.imovcg.back.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponseDTO> entrarComGoogle(@RequestBody @Valid GoogleLoginDTO dto) {
        return ResponseEntity.ok(authService.entrarComGoogle(dto.getIdToken()));
    }

    @GetMapping("/eu")
    public ResponseEntity<AnuncianteDTO> getPerfil(@AuthenticationPrincipal Long anuncianteId) {
        return ResponseEntity.ok(authService.getPerfil(anuncianteId));
    }

    @PatchMapping("/eu")
    public ResponseEntity<AnuncianteDTO> atualizarPerfil(
            @AuthenticationPrincipal Long anuncianteId,
            @RequestBody @Valid AnunciantePerfilDTO dto) {
        return ResponseEntity.ok(authService.atualizarTelefone(anuncianteId, dto.getTelefone()));
    }
}
