package com.imovcg.back.service;

import com.imovcg.back.completude.CalculadoraCompletudeImovel;
import com.imovcg.back.model.Imovel;
import com.imovcg.back.repository.ImovelRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Preenche a completude apenas dos anúncios que ainda não foram avaliados. */
@Service
public class CompletudeBackfillService {

    private final ImovelRepository imovelRepository;

    public CompletudeBackfillService(ImovelRepository imovelRepository) {
        this.imovelRepository = imovelRepository;
    }

    /**
     * Processa lotes fixos sempre a partir dos registros nulos. A operação pode ser repetida
     * sem alterar anúncios já avaliados.
     *
     * @return quantidade de imóveis atualizados
     */
    @Transactional
    public int executar() {
        int totalAtualizado = 0;
        List<Imovel> lote = imovelRepository.findTop100ByCompletudeIsNullOrderByIdAsc();

        while (!lote.isEmpty()) {
            for (Imovel imovel : lote) {
                int percentual = CalculadoraCompletudeImovel.avaliar(imovel).percentual();
                imovel.setCompletude(percentual);
            }
            imovelRepository.saveAllAndFlush(lote);
            totalAtualizado += lote.size();
            lote = imovelRepository.findTop100ByCompletudeIsNullOrderByIdAsc();
        }

        return totalAtualizado;
    }
}
