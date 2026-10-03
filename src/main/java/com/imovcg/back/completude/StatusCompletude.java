package com.imovcg.back.completude;

/** Classificação textual da completude de um anúncio. */
public enum StatusCompletude {
    INCOMPLETO,
    PARCIALMENTE_COMPLETO,
    COMPLETO;

    /** Converte um percentual nas faixas aprovadas para o MVP. */
    public static StatusCompletude dePercentual(int percentual) {
        if (percentual >= 80) {
            return COMPLETO;
        }
        if (percentual >= 50) {
            return PARCIALMENTE_COMPLETO;
        }
        return INCOMPLETO;
    }
}
