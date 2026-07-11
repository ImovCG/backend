package com.imovcg.back.dto;

import lombok.Data;

@Data
public class ImoveisFiltrosDTO {
    private Double precoMin;
    private Double precoMax;
    private String tipoImovel;
    private String cidade;
    private String bairro;
    private Integer quartos;
}
