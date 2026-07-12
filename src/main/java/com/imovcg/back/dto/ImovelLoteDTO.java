package com.imovcg.back.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ImovelLoteDTO {

    @Valid
    @NotEmpty
    private List<ImovelPostDTO> imoveis;
}