package com.imovcg.back.controller;

import com.imovcg.back.dto.ImoveisFiltros;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/imoveis")
public class ImovelController {

    @GetMapping("")
    public String getImoveis(@RequestBody ImoveisFiltros filtros) {
        return new String("imoveis");
    }

    @GetMapping("/{id}")
    public String getImovel(@PathVariable Long id) {
        return new String();
    }
    
    @PostMapping("")
    public String createImovel(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
}
