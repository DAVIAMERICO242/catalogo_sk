package com.skyler.catalogo.domain.descontos.carrinho.controllers;

import com.skyler.catalogo.domain.descontos.carrinho.services.DescontoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/desconto")
public class DescontoController {

    private final DescontoService descontoService;

    public DescontoController(DescontoService descontoService) {
        this.descontoService = descontoService;
    }


//    @GetMapping("/nivel-loja")
//    public ResponseEntity getNivelLoja(String lojaId){
//        try{
//            return ResponseEntity.ok().body(this.descontoCarrinhoService.getDescontos(lojaId));
//        }catch (Exception e){
//            return ResponseEntity.status(500).body(e.getLocalizedMessage());
//        }
//    }
//
//    @PostMapping("/nivel-loja")
//    public ResponseEntity criarDescontoLoja(@RequestBody DescontoCarrinhoDTO payload){
//        try{
//            return ResponseEntity.ok().body(this.descontoCarrinhoService.criarAtualizarDesconto(payload));
//        }catch (Exception e){
//            return ResponseEntity.status(500).body(e.getLocalizedMessage());
//        }
//    }
}
