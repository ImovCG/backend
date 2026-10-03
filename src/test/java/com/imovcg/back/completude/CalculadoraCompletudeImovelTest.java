package com.imovcg.back.completude;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.imovcg.back.model.Imovel;
import com.imovcg.back.model.ImovelFoto;
import java.util.List;
import org.junit.jupiter.api.Test;

class CalculadoraCompletudeImovelTest {

    @Test
    void deveCalcularCemPorCentoParaImovelCompleto() {
        Imovel imovel = imovelCompleto();

        AvaliacaoCompletude resultado = CalculadoraCompletudeImovel.avaliar(imovel);

        assertEquals(100, resultado.percentual());
        assertEquals(StatusCompletude.COMPLETO, resultado.status());
        assertTrue(resultado.camposFaltantes().isEmpty());
    }

    @Test
    void deveListarTodosOsCamposParaImovelVazio() {
        AvaliacaoCompletude resultado = CalculadoraCompletudeImovel.avaliar(new Imovel());

        assertEquals(0, resultado.percentual());
        assertEquals(StatusCompletude.INCOMPLETO, resultado.status());
        assertEquals(List.of(
                "titulo", "preco", "tipoAnuncio", "categoria",
                "endereco", "bairro", "cidade", "estado",
                "quartos", "banheiros", "areaM2", "vagas",
                "descricao", "fotos"), resultado.camposFaltantes());
    }

    @Test
    void deveAceitarZeroParaQuantidadesMasNaoParaPrecoOuArea() {
        Imovel imovel = new Imovel();
        imovel.setPreco(0.0);
        imovel.setAreaM2(0.0);
        imovel.setQuartos(0);
        imovel.setBanheiros(0);
        imovel.setVagas(0);

        AvaliacaoCompletude resultado = CalculadoraCompletudeImovel.avaliar(imovel);

        assertEquals(23, resultado.percentual());
        assertTrue(resultado.camposFaltantes().contains("preco"));
        assertTrue(resultado.camposFaltantes().contains("areaM2"));
        assertFalse(resultado.camposFaltantes().contains("quartos"));
        assertFalse(resultado.camposFaltantes().contains("banheiros"));
        assertFalse(resultado.camposFaltantes().contains("vagas"));
    }

    @Test
    void deveValidarTextosDescricaoEFotosSemanticamente() {
        Imovel imovel = new Imovel();
        imovel.setTitulo("   ");
        imovel.setDescricao("12345678901234567890123456789");

        ImovelFoto fotoVazia = new ImovelFoto();
        fotoVazia.setUrl("  ");
        imovel.setFotos(List.of(fotoVazia));

        AvaliacaoCompletude incompleto = CalculadoraCompletudeImovel.avaliar(imovel);
        assertTrue(incompleto.camposFaltantes().contains("titulo"));
        assertTrue(incompleto.camposFaltantes().contains("descricao"));
        assertTrue(incompleto.camposFaltantes().contains("fotos"));

        imovel.setTitulo("Apartamento");
        imovel.setDescricao("123456789012345678901234567890");
        fotoVazia.setUrl("https://example.com/foto.jpg");

        AvaliacaoCompletude preenchido = CalculadoraCompletudeImovel.avaliar(imovel);
        assertFalse(preenchido.camposFaltantes().contains("titulo"));
        assertFalse(preenchido.camposFaltantes().contains("descricao"));
        assertFalse(preenchido.camposFaltantes().contains("fotos"));
    }

    @Test
    void deveArredondarPercentualParaInteiro() {
        Imovel imovel = new Imovel();
        imovel.setEndereco("Rua A");
        assertEquals(6, CalculadoraCompletudeImovel.avaliar(imovel).percentual());

        imovel.setTitulo("Apartamento");
        assertEquals(14, CalculadoraCompletudeImovel.avaliar(imovel).percentual());
    }

    @Test
    void deveClassificarOsLimitesAprovados() {
        assertEquals(StatusCompletude.INCOMPLETO, StatusCompletude.dePercentual(49));
        assertEquals(StatusCompletude.PARCIALMENTE_COMPLETO,
                StatusCompletude.dePercentual(50));
        assertEquals(StatusCompletude.PARCIALMENTE_COMPLETO,
                StatusCompletude.dePercentual(79));
        assertEquals(StatusCompletude.COMPLETO, StatusCompletude.dePercentual(80));
    }

    private Imovel imovelCompleto() {
        Imovel imovel = new Imovel();
        imovel.setTitulo("Apartamento próximo à UFCG");
        imovel.setPreco(1200.0);
        imovel.setTipoAnuncio("aluguel");
        imovel.setCategoria("apartamento");
        imovel.setEndereco("Rua Aprígio Veloso, 882");
        imovel.setBairro("Universitário");
        imovel.setCidade("Campina Grande");
        imovel.setEstado("PB");
        imovel.setQuartos(2);
        imovel.setBanheiros(1);
        imovel.setAreaM2(55.0);
        imovel.setVagas(1);
        imovel.setDescricao("Apartamento mobiliado e próximo ao campus da universidade.");

        ImovelFoto foto = new ImovelFoto();
        foto.setUrl("https://example.com/foto.jpg");
        imovel.setFotos(List.of(foto));
        return imovel;
    }
}
