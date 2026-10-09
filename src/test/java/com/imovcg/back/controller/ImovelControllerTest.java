package com.imovcg.back.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.imovcg.back.dto.ImoveisFiltrosDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.model.Imovel;
import com.imovcg.back.service.ImovelService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ImovelController.class)
class ImovelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ImovelService imovelService;

    @Test
    void deveFiltrarImoveisPorFonteSemImporOrdenacaoPublica() throws Exception {
        when(imovelService.getImoveis(any(ImoveisFiltrosDTO.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/imoveis/fonte/facebook"))
                .andExpect(status().isOk());

        ArgumentCaptor<ImoveisFiltrosDTO> filtrosCaptor =
                ArgumentCaptor.forClass(ImoveisFiltrosDTO.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(imovelService).getImoveis(filtrosCaptor.capture(), pageableCaptor.capture());
        assertEquals("facebook", filtrosCaptor.getValue().getFonte());
        assertTrue(pageableCaptor.getValue().getSort().isUnsorted());
    }

    @Test
    void deveAplicarOrdenacaoPadraoDeCompletudeNaBuscaPublica() throws Exception {
        when(imovelService.getImoveis(any(ImoveisFiltrosDTO.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/imoveis"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(imovelService).getImoveis(any(ImoveisFiltrosDTO.class), captor.capture());
        Sort sort = captor.getValue().getSort();
        assertOrdemDescendente(sort, "completude");
        assertEquals(Sort.NullHandling.NULLS_LAST,
                sort.getOrderFor("completude").getNullHandling());
        assertOrdemDescendente(sort, "updatedAt");
        assertOrdemDescendente(sort, "id");
    }

    @Test
    void devePreservarOrdenacaoInformadaPeloCliente() throws Exception {
        when(imovelService.getImoveis(any(ImoveisFiltrosDTO.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/imoveis").param("sort", "preco,asc"))
                .andExpect(status().isOk());

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        verify(imovelService).getImoveis(any(ImoveisFiltrosDTO.class), captor.capture());
        Sort.Order preco = captor.getValue().getSort().getOrderFor("preco");
        assertNotNull(preco);
        assertEquals(Sort.Direction.ASC, preco.getDirection());
        assertEquals(1, captor.getValue().getSort().stream().count());
    }

    @Test
    void deveAceitarFiltroPorUniversidadeEDistanciaMaxima() throws Exception {
        when(imovelService.getImoveis(any(ImoveisFiltrosDTO.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/api/imoveis")
                        .param("universidade", "uepb")
                        .param("distanciaMaximaKm", "5"))
                .andExpect(status().isOk());

        ArgumentCaptor<ImoveisFiltrosDTO> captor =
                ArgumentCaptor.forClass(ImoveisFiltrosDTO.class);
        verify(imovelService).getImoveis(captor.capture(), any(Pageable.class));
        assertEquals("uepb", captor.getValue().getUniversidade());
        assertEquals(5.0, captor.getValue().getDistanciaMaximaKm());
    }

    @Test
    void deveRejeitarDistanciaSemUniversidade() throws Exception {
        mockMvc.perform(get("/api/imoveis").param("distanciaMaximaKm", "5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRejeitarUniversidadeDesconhecida() throws Exception {
        mockMvc.perform(get("/api/imoveis").param("universidade", "UFC"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveSerializarDadosDeCompletudeNoDetalhe() throws Exception {
        Imovel imovel = new Imovel();
        imovel.setId(42L);
        imovel.setTitulo("Apartamento");
        imovel.setCompletude(8);
        when(imovelService.getImovel(42L)).thenReturn(new ImovelGetDTO(imovel));

        mockMvc.perform(get("/api/imoveis/42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completude").value(8))
                .andExpect(jsonPath("$.statusCompletude").value("INCOMPLETO"))
                .andExpect(jsonPath("$.camposFaltantes[0]").value("preco"));
    }

    private void assertOrdemDescendente(Sort sort, String campo) {
        Sort.Order order = sort.getOrderFor(campo);
        assertNotNull(order);
        assertEquals(Sort.Direction.DESC, order.getDirection());
    }
}
