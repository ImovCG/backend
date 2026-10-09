package com.imovcg.back.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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
    private String fonte;
    @Pattern(regexp = "(?i)^(UFCG|UEPB|IFPB)$",
            message = "universidade deve ser UFCG, UEPB ou IFPB")
    private String universidade;
    @PositiveOrZero
    private Double distanciaMaximaKm;
}
