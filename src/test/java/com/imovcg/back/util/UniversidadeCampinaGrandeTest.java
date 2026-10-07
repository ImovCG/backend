package com.imovcg.back.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.imovcg.back.dto.ImovelGetDTO;
import com.imovcg.back.model.Imovel;
import org.junit.jupiter.api.Test;

class UniversidadeCampinaGrandeTest {

    @Test
    void deveCalcularDistanciaZeroNasCoordenadasDoCampus() {
        for (UniversidadeCampinaGrande universidade : UniversidadeCampinaGrande.values()) {
            assertEquals(0.0, universidade.distanciaEmKm(
                    universidade.getLatitude(), universidade.getLongitude()), 0.000001);
        }
    }

    @Test
    void deveCalcularDistanciaEmQuilometrosEntreCampi() {
        double distancia = UniversidadeCampinaGrande.UFCG.distanciaEmKm(
                UniversidadeCampinaGrande.UEPB.getLatitude(),
                UniversidadeCampinaGrande.UEPB.getLongitude());

        assertTrue(distancia > 1.0 && distancia < 2.0);
    }

    @Test
    void deveResolverUniversidadeSemDiferenciarMaiusculas() {
        assertEquals(UniversidadeCampinaGrande.IFPB,
                UniversidadeCampinaGrande.from("ifpb"));
    }

    @Test
    void deveExporDistanciaNoDtoQuandoUniversidadeForSelecionada() {
        Imovel imovel = new Imovel();
        imovel.setLatitude(UniversidadeCampinaGrande.UFCG.getLatitude());
        imovel.setLongitude(UniversidadeCampinaGrande.UFCG.getLongitude());

        ImovelGetDTO resposta = new ImovelGetDTO(imovel, UniversidadeCampinaGrande.UFCG);

        assertEquals(0.0, resposta.getDistanciaUniversidadeKm());
    }
}
