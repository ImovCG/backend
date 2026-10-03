package com.imovcg.back.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.imovcg.back.completude.StatusCompletude;
import com.imovcg.back.dto.AnuncioDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.dto.ImovelPostDTO;
import com.imovcg.back.model.Anunciante;
import com.imovcg.back.model.Imovel;
import com.imovcg.back.repository.ImovelRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;

class CompletudeFluxosServiceTest {

    private final ImovelRepository imovelRepository = mock(ImovelRepository.class);
    private final AuthService authService = mock(AuthService.class);

    private ImovelService imovelService;
    private AnuncioService anuncioService;

    @BeforeEach
    void setUp() {
        imovelService = new ImovelService(imovelRepository, new ModelMapper());
        anuncioService = new AnuncioService(imovelRepository, authService);
    }

    @Test
    void devePersistirCompletudeNaIngestaoDoScraper() {
        ImovelPostDTO dto = imovelRaspadoCompleto();
        when(imovelRepository.findByFonteAndExternalId("olx", "123"))
                .thenReturn(Optional.empty());
        when(imovelRepository.findByExternalId("123")).thenReturn(Optional.empty());
        when(imovelRepository.findByHash(any())).thenReturn(Optional.empty());

        ImovelGetDTO resposta = imovelService.saveImovel(dto);

        ArgumentCaptor<Imovel> captor = ArgumentCaptor.forClass(Imovel.class);
        verify(imovelRepository).save(captor.capture());
        assertEquals(100, captor.getValue().getCompletude());
        assertEquals(100, resposta.getCompletude());
        assertEquals(StatusCompletude.COMPLETO, resposta.getStatusCompletude());
        assertTrue(resposta.getCamposFaltantes().isEmpty());
    }

    @Test
    void devePersistirCompletudeNoCadastroManual() {
        Anunciante anunciante = new Anunciante();
        when(authService.buscarAnunciante(7L)).thenReturn(anunciante);

        AnuncioDTO dto = new AnuncioDTO();
        dto.setTitulo("Apartamento próximo à UFCG");
        dto.setPreco(1200.0);
        dto.setEndereco("Rua Aprígio Veloso, 882");
        dto.setBairro("Universitário");
        dto.setTipoAnuncio("aluguel");
        dto.setCategoria("apartamento");

        ImovelGetDTO resposta = anuncioService.criar(7L, dto);

        ArgumentCaptor<Imovel> captor = ArgumentCaptor.forClass(Imovel.class);
        verify(imovelRepository).save(captor.capture());
        assertEquals(55, captor.getValue().getCompletude());
        assertEquals(55, resposta.getCompletude());
        assertEquals(StatusCompletude.PARCIALMENTE_COMPLETO,
                resposta.getStatusCompletude());
        assertTrue(resposta.getCamposFaltantes().contains("areaM2"));
        assertTrue(resposta.getCamposFaltantes().contains("fotos"));
    }

    private ImovelPostDTO imovelRaspadoCompleto() {
        ImovelPostDTO dto = new ImovelPostDTO();
        dto.setExternalId("123");
        dto.setFonte("olx");
        dto.setTitulo("Apartamento próximo à UFCG");
        dto.setPreco(1200.0);
        dto.setEndereco("Rua Aprígio Veloso, 882");
        dto.setUrl("https://olx.example/123");
        dto.setEstado("PB");
        dto.setTipoAnuncio("aluguel");
        dto.setCategoria("apartamento");
        dto.setCidade("Campina Grande");
        dto.setBairro("Universitário");
        dto.setQuartos(2);
        dto.setBanheiros(1);
        dto.setAreaM2(55.0);
        dto.setVagas(1);
        dto.setDataColeta(LocalDate.now());
        dto.setDescricao("Apartamento mobiliado e próximo ao campus da universidade.");
        dto.setFotos(List.of("https://example.com/foto.jpg"));
        return dto;
    }
}
