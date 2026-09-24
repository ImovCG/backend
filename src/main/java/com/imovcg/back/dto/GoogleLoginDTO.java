package com.imovcg.back.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginDTO {

    @NotBlank
    private String idToken;
}
