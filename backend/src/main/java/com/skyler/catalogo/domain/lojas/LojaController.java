package com.skyler.catalogo.domain.lojas;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;

    public LojaController(LojaService lojaService) {
        this.lojaService = lojaService;
    }

    @GetMapping
    public ResponseEntity getLojas(){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojas());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/matriz")
    public ResponseEntity getLojasMatriz(){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojasMatriz());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping("/franquia")
    public ResponseEntity getFranquias(){
        try{
            return ResponseEntity.ok().body(this.lojaService.getLojasFranquia());
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
