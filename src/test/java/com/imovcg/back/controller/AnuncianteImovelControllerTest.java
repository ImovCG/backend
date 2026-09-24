package com.imovcg.back.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.imovcg.back.model.Anunciante;
import com.imovcg.back.security.SecurityConfig;
import com.imovcg.back.security.JwtService;
import com.imovcg.back.service.AnuncioService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AnuncianteImovelController.class)
@Import(SecurityConfig.class)
class AnuncianteImovelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private AnuncioService anuncioService;

    @Test
    void deveRecusarPainelSemToken() throws Exception {
        mockMvc.perform(get("/api/anunciante/imoveis"))
            .andExpect(status().isUnauthorized());

        verifyNoInteractions(anuncioService);
    }

    @Test
    void deveRecusarPainelComTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/anunciante/imoveis")
                .header("Authorization", "Bearer token.qualquer.invalido"))
            .andExpect(status().isUnauthorized());

        verifyNoInteractions(anuncioService);
    }

    @Test
    void deveListarApenasOsImoveisDoDonoDoToken() throws Exception {
        Anunciante anunciante = new Anunciante();
        anunciante.setId(42L);
        anunciante.setEmail("dono@exemplo.com");
        anunciante.setNome("Dono");

        when(anuncioService.listarMeus(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/anunciante/imoveis")
                .header("Authorization", "Bearer " + jwtService.gerarToken(anunciante)))
            .andExpect(status().isOk());

        verify(anuncioService).listarMeus(eq(42L));
    }
}
