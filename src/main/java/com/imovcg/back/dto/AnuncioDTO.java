package com.imovcg.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

/**
 * Anuncio criado a mao pelo proprio anunciante, sem os campos de coleta automatica.
 */
@Data
public class AnuncioDTO {

    @NotBlank
    @Size(max = 255)
    private String titulo;

    @NotNull
    @Positive
    private Double preco;

    @NotBlank
    @Size(max = 255)
    private String endereco;

    @NotBlank
    @Size(max = 255)
    private String bairro;

    @Size(max = 255)
    private String cidade;

    @Size(max = 255)
    private String estado;

    @NotBlank
    private String tipoAnuncio;

    @NotBlank
    private String categoria;

    private Double latitude;
    private Double longitude;

    @PositiveOrZero
    private Integer quartos;

    @PositiveOrZero
    private Integer banheiros;

    @PositiveOrZero
    private Double areaM2;

    @PositiveOrZero
    private Double condominio;

    @PositiveOrZero
    private Double iptu;

    @PositiveOrZero
    private Integer vagas;

    private String descricao;

    private List<String> fotos;
}
