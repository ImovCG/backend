package com.imovcg.back.dto;

import com.imovcg.back.model.Anunciante;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnuncianteDTO {
    private Long id;
    private String nome;
    private String email;
    private String fotoUrl;
    private String telefone;

    public AnuncianteDTO(Anunciante anunciante) {
        this.id = anunciante.getId();
        this.nome = anunciante.getNome();
        this.email = anunciante.getEmail();
        this.fotoUrl = anunciante.getFotoUrl();
        this.telefone = anunciante.getTelefone();
    }
}
