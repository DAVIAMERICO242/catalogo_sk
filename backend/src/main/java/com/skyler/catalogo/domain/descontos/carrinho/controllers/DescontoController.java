package com.skyler.catalogo.domain.descontos.carrinho.controllers;

import com.skyler.catalogo.domain.descontos.carrinho.DTOs.DescontoDTO;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoRepository;
import com.skyler.catalogo.domain.descontos.carrinho.services.DescontoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/descontos")
public class DescontoController {

    private final DescontoService descontoService;
    private final DescontoRepository descontoRepository;

    public DescontoController(DescontoService descontoService, DescontoRepository descontoRepository) {
        this.descontoService = descontoService;
        this.descontoRepository = descontoRepository;
    }


    @GetMapping
    public ResponseEntity getDescontos(@RequestParam String lojaId){
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

    @DeleteMapping
    public ResponseEntity deletar(String id){
        try{
            this.descontoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
