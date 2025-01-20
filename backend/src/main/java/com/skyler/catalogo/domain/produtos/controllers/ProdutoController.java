package com.skyler.catalogo.domain.produtos.controllers;

import com.skyler.catalogo.domain.produtos.DTOs.ProdutoDTO;
import com.skyler.catalogo.domain.produtos.services.ProdutoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping
    public ResponseEntity getProdutos(String franquiaSystemId){
        try{
            return ResponseEntity.ok().body(this.produtoService.getProdutos(franquiaSystemId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/variacao")
    public ResponseEntity getVariacoes(String productId){
        try{
            return ResponseEntity.ok().body(this.produtoService.getVariacao(productId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/estoque")
    public ResponseEntity getEstoque(@RequestParam List<String> skusBase, String lojaSlug){
        try{
            return ResponseEntity.ok().body(this.produtoService.getEstoque(skusBase,lojaSlug));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
