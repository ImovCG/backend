package com.imovcg.back.dto;

import com.imovcg.back.model.Imovel;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImovelGetDTO {
    private Long id;
    private String titulo;
    private Double preco;
    private String endereco;
    private String tipo;
    private String url;

    public ImovelGetDTO (Imovel imovel) {
        this.id = imovel.getId();
        this.titulo = imovel.getTitulo();
        this.preco = imovel.getPreco();
        this.endereco = imovel.getEndereco();
        this.tipo = imovel.getTipo();
        this.url = imovel.getUrl();
    }
}
