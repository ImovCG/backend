package com.imovcg.back.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Dados do anunciante que ele mesmo edita. O nome e o e-mail vem do Google e nao sao alterados aqui.
 */
@Data
public class AnunciantePerfilDTO {

    @Size(max = 30)
    @Pattern(
        regexp = "^$|^[0-9()+\\-.\\s]{8,30}$",
        message = "Telefone deve conter apenas numeros e os simbolos ( ) + - .")
    private String telefone;
}
