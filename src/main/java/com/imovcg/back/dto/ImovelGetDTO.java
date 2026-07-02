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

    public ImovelGetDTO (Imovel imovel) {
        this.id = imovel.getId();
        this.titulo = imovel.getTitulo();
        this.preco = imovel.getPreco();
        this.endereco = imovel.getEndereco();
        this.tipo = imovel.getTipo();
        this.url = imovel.getUrl();
        this.externalId = imovel.getExternalId();
        this.tipoAnuncio = imovel.getTipoAnuncio();
        this.categoria = imovel.getCategoria();
        this.cidade = imovel.getCidade();
        this.bairro = imovel.getBairro();
        this.quartos = imovel.getQuartos();
        this.banheiros = imovel.getBanheiros();
        this.areaM2 = imovel.getAreaM2();
        this.condominio = imovel.getCondominio();
        this.iptu = imovel.getIptu();
        this.vagas = imovel.getVagas();
        this.dataColeta = imovel.getDataColeta();
        this.descricao = imovel.getDescricao();
    }
}
