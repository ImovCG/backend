package com.imovcg.back.dto;

import lombok.Data;

@Data
public class ImoveisFiltrosDTO {
    private Double precoMin;
    private Double precoMax;
    private String cidade;
    private String bairro;
    private Integer quartos;
    private Integer quartosMin;
    private Integer banheirosMin;
    private Double areaMin;
    private String categoria;
}
