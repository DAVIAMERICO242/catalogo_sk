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

    @GetMapping("/ativos/by-slug")
    public ResponseEntity getDescontosBySlug(String lojaSlug){
        try{
            return ResponseEntity.ok().body(this.descontoService.getDescontosAtivosByLojaSlug(lojaSlug));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }


    @GetMapping
    public ResponseEntity getDescontos(
            @RequestParam(required = false) String lojaId,
            @RequestParam(required = true) String franquiaId
            ){
        try{
            return ResponseEntity.ok().body(this.descontoService.getDescontos(lojaId,franquiaId));
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
