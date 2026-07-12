package com.imovcg.back.dto;

import com.imovcg.back.model.Imovel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImovelGetDTO {
    private Long id;
    private String externalId;
    private String fonte;
    private String titulo;
    private Double preco;
    private String endereco;
    private String url;
    private String estado;
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
    private LocalDate dataColeta;
    private String descricao;
    private List<String> fotos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ImovelGetDTO (Imovel imovel) {
        this.id = imovel.getId();
        this.externalId = imovel.getExternalId();
        this.fonte = imovel.getFonte();
        this.titulo = imovel.getTitulo();
        this.preco = imovel.getPreco();
        this.endereco = imovel.getEndereco();
        this.url = imovel.getUrl();
        this.estado = imovel.getEstado();
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
        this.fotos = imovel.getFotos().stream().map(foto -> foto.getUrl()).toList();
        this.createdAt = imovel.getCreatedAt();
        this.updatedAt = imovel.getUpdatedAt();
    }
}
