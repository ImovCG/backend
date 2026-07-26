package com.imovcg.back.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import com.imovcg.back.dto.ImoveisFiltrosDTO;
import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.service.ImovelService;

@WebMvcTest(ImovelController.class)
class ImovelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImovelService imovelService;

    @Test
    void deveFiltrarImoveisPorFonteNaRotaEspecifica() throws Exception {
        when(imovelService.getImoveis(any(ImoveisFiltrosDTO.class), any(Pageable.class)))
            .thenReturn(Page.empty());

        mockMvc.perform(get("/api/imoveis/fonte/facebook"))
            .andExpect(status().isOk());

        ArgumentCaptor<ImoveisFiltrosDTO> filtrosCaptor = ArgumentCaptor.forClass(ImoveisFiltrosDTO.class);
        verify(imovelService).getImoveis(filtrosCaptor.capture(), any(Pageable.class));
        assertEquals("facebook", filtrosCaptor.getValue().getFonte());
    }
}
