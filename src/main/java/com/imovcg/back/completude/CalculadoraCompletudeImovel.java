package com.imovcg.back.completude;

import com.imovcg.back.model.Imovel;
import com.imovcg.back.model.ImovelFoto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Calcula a completude usando uma regra única para anúncios manuais e raspados.
 * Condomínio e IPTU não pontuam porque podem legitimamente não se aplicar ao imóvel.
 */
public final class CalculadoraCompletudeImovel {

    private static final double PESO_BASICO = 7.5;
    private static final double PESO_LOCALIZACAO = 6.25;
    private static final double PESO_CARACTERISTICA = 7.5;
    private static final double PESO_CONTEUDO = 7.5;
    private static final int TAMANHO_MINIMO_DESCRICAO = 30;

    private CalculadoraCompletudeImovel() {
    }

    /** Avalia os campos canônicos do imóvel e arredonda o resultado para um inteiro. */
    public static AvaliacaoCompletude avaliar(Imovel imovel) {
        Objects.requireNonNull(imovel, "imovel não pode ser nulo");

        List<String> faltantes = new ArrayList<>();
        double pontos = 0.0;

        pontos += pontuar(textoPreenchido(imovel.getTitulo()), PESO_BASICO,
                "titulo", faltantes);
        pontos += pontuar(numeroPositivo(imovel.getPreco()), PESO_BASICO,
                "preco", faltantes);
        pontos += pontuar(textoPreenchido(imovel.getTipoAnuncio()), PESO_BASICO,
                "tipoAnuncio", faltantes);
        pontos += pontuar(textoPreenchido(imovel.getCategoria()), PESO_BASICO,
                "categoria", faltantes);

        pontos += pontuar(textoPreenchido(imovel.getEndereco()), PESO_LOCALIZACAO,
                "endereco", faltantes);
        pontos += pontuar(textoPreenchido(imovel.getBairro()), PESO_LOCALIZACAO,
                "bairro", faltantes);
        pontos += pontuar(textoPreenchido(imovel.getCidade()), PESO_LOCALIZACAO,
                "cidade", faltantes);
        pontos += pontuar(textoPreenchido(imovel.getEstado()), PESO_LOCALIZACAO,
                "estado", faltantes);

        pontos += pontuar(imovel.getQuartos() != null, PESO_CARACTERISTICA,
                "quartos", faltantes);
        pontos += pontuar(imovel.getBanheiros() != null, PESO_CARACTERISTICA,
                "banheiros", faltantes);
        pontos += pontuar(numeroPositivo(imovel.getAreaM2()), PESO_CARACTERISTICA,
                "areaM2", faltantes);
        pontos += pontuar(imovel.getVagas() != null, PESO_CARACTERISTICA,
                "vagas", faltantes);

        boolean descricaoValida = textoPreenchido(imovel.getDescricao())
                && imovel.getDescricao().strip().length() >= TAMANHO_MINIMO_DESCRICAO;
        pontos += pontuar(descricaoValida, PESO_CONTEUDO, "descricao", faltantes);
        pontos += pontuar(temFotoValida(imovel.getFotos()), PESO_CONTEUDO,
                "fotos", faltantes);

        int percentual = (int) Math.round(pontos);
        return new AvaliacaoCompletude(
                percentual,
                StatusCompletude.dePercentual(percentual),
                faltantes);
    }

    private static double pontuar(
            boolean preenchido,
            double peso,
            String campo,
            List<String> faltantes) {
        if (preenchido) {
            return peso;
        }
        faltantes.add(campo);
        return 0.0;
    }

    private static boolean textoPreenchido(String valor) {
        return valor != null && !valor.isBlank();
    }

    private static boolean numeroPositivo(Double valor) {
        return valor != null && valor > 0;
    }

    private static boolean temFotoValida(List<ImovelFoto> fotos) {
        return fotos != null && fotos.stream()
                .filter(Objects::nonNull)
                .map(ImovelFoto::getUrl)
                .anyMatch(CalculadoraCompletudeImovel::textoPreenchido);
    }
}
