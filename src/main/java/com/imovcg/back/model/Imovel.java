package com.imovcg.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Imovel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 64)
    private String hash;
    private String titulo;
    private Double preco;
    private String endereco;
    private String tipoImovel;
    private String url;

    // Campos adicionais coletados via web scraping
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
