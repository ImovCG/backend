package com.imovcg.back.completude;

import java.util.List;

/** Resultado imutável da avaliação de completude de um imóvel. */
public record AvaliacaoCompletude(
        int percentual,
        StatusCompletude status,
        List<String> camposFaltantes) {

    public AvaliacaoCompletude {
        camposFaltantes = List.copyOf(camposFaltantes);
    }
}
