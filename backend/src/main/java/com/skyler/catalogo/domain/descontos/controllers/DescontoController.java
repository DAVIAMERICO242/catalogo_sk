package com.skyler.catalogo.domain.descontos.controllers;

import com.skyler.catalogo.domain.descontos.DTOs.DescontoDTO;
import com.skyler.catalogo.domain.descontos.services.DescontoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/descontos")
public class DescontoController {

    private final DescontoService descontoService;

    public DescontoController(DescontoService descontoService) {
        this.descontoService = descontoService;
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
            this.descontoService.deletarDesconto(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
