package com.imovcg.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ImovelPostDTO {
    
    @NotBlank
    private String titulo;
    
    @Positive
    private Double preco;
    
    @NotBlank
    private String endereco;
    
    @NotBlank
    private String tipo;
    
    @NotBlank
    private String url;
    private String externalId;
    private String tipoAnuncio;
    private String categoria;
    private String cidade;
    private String bairro;
    private Integer quartos;
    private Integer banheiros;
    private Double areaM2;
    private Double condominio;
    private Double iptu;
    private Integer vagas;
    private String dataColeta;
    private String descricao;

}
