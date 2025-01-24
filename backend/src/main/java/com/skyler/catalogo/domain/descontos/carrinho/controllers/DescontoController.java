package com.skyler.catalogo.domain.descontos.carrinho.controllers;

import com.skyler.catalogo.domain.descontos.carrinho.DTOs.DescontoDTO;
import com.skyler.catalogo.domain.descontos.carrinho.services.DescontoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/desconto")
public class DescontoController {

    private final DescontoService descontoService;

    public DescontoController(DescontoService descontoService) {
        this.descontoService = descontoService;
    }


    @GetMapping
    public ResponseEntity getDescontos(String lojaId){
        try{
            return ResponseEntity.ok().body(this.descontoService.getDescontosForLoja(lojaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping
    public ResponseEntity criarAtualizarDesconto(@RequestBody DescontoDTO descontoDTO){
        try{
            return ResponseEntity.ok().body(this.descontoService.cadastrarAtualizarDesconto(descontoDTO));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
