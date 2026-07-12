package com.imovcg.back.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ImovelPostDTO {
    
    @NotBlank
    private String externalId;

    @NotBlank
    private String fonte;

    @NotBlank
    private String titulo;
    
    @NotNull
    @Positive
    private Double preco;
    
    @NotBlank
    private String endereco;
    
    
    @NotBlank
    private String url;
    private String estado;
    private String tipoAnuncio;
    private String categoria;
    private String cidade;
    private String bairro;
    private Integer quartos;
    private Integer banheiros;
    private Double areaM2;

    @PositiveOrZero
    private Double condominio;

    @PositiveOrZero
    private Double iptu;
    private Integer vagas;

    @NotNull
    private LocalDate dataColeta;
    private String descricao;
    private List<String> fotos;

}
