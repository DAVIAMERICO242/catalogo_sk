package com.skyler.catalogo.domain.produtos.controllers;

import com.skyler.catalogo.domain.produtos.services.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/atributos")
    public ResponseEntity getCategorias(String franquiaSystemId){
        try{
            return ResponseEntity.ok().body(this.produtoService.getAtributos(franquiaSystemId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
