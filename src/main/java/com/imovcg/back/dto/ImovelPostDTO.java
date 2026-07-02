package com.imovcg.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
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

    @NotBlank
    @Email
    private String email;

}
