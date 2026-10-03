package com.imovcg.back.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.imovcg.back.model.Imovel;
import com.imovcg.back.repository.ImovelRepository;
import java.util.List;
import org.junit.jupiter.api.Test;

class CompletudeBackfillServiceTest {

    private final ImovelRepository imovelRepository = mock(ImovelRepository.class);

    @Test
    void deveAtualizarSomenteRegistrosSemCompletudeEmLotes() {
        Imovel primeiro = new Imovel();
        primeiro.setTitulo("Apartamento");
        Imovel segundo = new Imovel();
        segundo.setEndereco("Rua A");

        when(imovelRepository.findTop100ByCompletudeIsNullOrderByIdAsc())
                .thenReturn(List.of(primeiro, segundo), List.of());
        CompletudeBackfillService service =
                new CompletudeBackfillService(imovelRepository);

        int atualizados = service.executar();

        assertEquals(2, atualizados);
        assertEquals(8, primeiro.getCompletude());
        assertEquals(6, segundo.getCompletude());
        verify(imovelRepository).saveAllAndFlush(List.of(primeiro, segundo));
    }

    @Test
    void deveSerIdempotenteQuandoNaoHaRegistrosPendentes() {
        when(imovelRepository.findTop100ByCompletudeIsNullOrderByIdAsc())
                .thenReturn(List.of());
        CompletudeBackfillService service =
                new CompletudeBackfillService(imovelRepository);

        assertEquals(0, service.executar());
    }
}
