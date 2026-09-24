package com.imovcg.back.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.imovcg.back.dto.AnuncianteDTO;
import com.imovcg.back.dto.AuthResponseDTO;
import com.imovcg.back.model.Anunciante;
import com.imovcg.back.repository.AnuncianteRepository;
import com.imovcg.back.security.JwtService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Troca o ID token do Google por uma sessao propria do imovCG.
 */
@Service
@Transactional
public class AuthService {

    private final AnuncianteRepository anuncianteRepository;
    private final JwtService jwtService;
    private final GoogleIdTokenVerifier verifier;

    public AuthService(
            AnuncianteRepository anuncianteRepository,
            JwtService jwtService,
            @Value("${google.client-id}") String clientId) {
        this.anuncianteRepository = anuncianteRepository;
        this.jwtService = jwtService;
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public AuthResponseDTO entrarComGoogle(String idToken) {
        GoogleIdToken.Payload payload = verificar(idToken);

        String googleSub = payload.getSubject();
        String email = payload.getEmail();
        String nome = (String) payload.get("name");
        String foto = (String) payload.get("picture");

        Anunciante anunciante = anuncianteRepository.findByGoogleSub(googleSub)
                .or(() -> anuncianteRepository.findByEmail(email))
                .orElseGet(Anunciante::new);

        anunciante.setGoogleSub(googleSub);
        anunciante.setEmail(email);
        anunciante.setNome(nome == null || nome.isBlank() ? email : nome);
        anunciante.setFotoUrl(foto);

        anuncianteRepository.save(anunciante);

        return new AuthResponseDTO(
                jwtService.gerarToken(anunciante),
                jwtService.getValidadeSegundos(),
                new AnuncianteDTO(anunciante));
    }

    @Transactional(readOnly = true)
    public AnuncianteDTO getPerfil(Long anuncianteId) {
        return new AnuncianteDTO(buscarAnunciante(anuncianteId));
    }

    public AnuncianteDTO atualizarTelefone(Long anuncianteId, String telefone) {
        Anunciante anunciante = buscarAnunciante(anuncianteId);
        anunciante.setTelefone(telefone == null || telefone.isBlank() ? null : telefone.trim());
        anuncianteRepository.save(anunciante);
        return new AnuncianteDTO(anunciante);
    }

    public Anunciante buscarAnunciante(Long anuncianteId) {
        return anuncianteRepository.findById(anuncianteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Anunciante nao encontrado"));
    }

    private GoogleIdToken.Payload verificar(String idToken) {
        GoogleIdToken token;
        try {
            token = verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Nao foi possivel validar o login do Google", e);
        }

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login do Google invalido");
        }

        GoogleIdToken.Payload payload = token.getPayload();

        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "E-mail do Google nao verificado");
        }

        return payload;
    }
}
