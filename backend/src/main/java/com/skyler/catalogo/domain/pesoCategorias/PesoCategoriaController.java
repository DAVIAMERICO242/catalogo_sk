package com.skyler.catalogo.domain.pesoCategorias;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/peso-categoria")
public class PesoCategoriaController {

    private final PesoCategoriasService pesoCategoriasService;

    public PesoCategoriaController(PesoCategoriasService pesoCategoriasService) {
        this.pesoCategoriasService = pesoCategoriasService;
    }

    @PostMapping
    public ResponseEntity cadastrarAtualizar(@RequestBody PesoCategoriasRequest pesoCategoriasRequest){
        try{
            return ResponseEntity.ok().body(this.pesoCategoriasService.cadastrarAtualizar(pesoCategoriasRequest));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity getPesos(String franquiaId){
        try{
            return ResponseEntity.ok().body(this.pesoCategoriasService.getPesos(franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
