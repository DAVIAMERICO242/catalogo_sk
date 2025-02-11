package com.skyler.catalogo.domain.correios;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/correio-franquia-context")
public class CorreioFranquiaController {

    private final CorreiosFranquiaContextService correiosFranquiaContextService;

    public CorreioFranquiaController(CorreiosFranquiaContextService correiosFranquiaContextService) {
        this.correiosFranquiaContextService = correiosFranquiaContextService;
    }

    @PostMapping
    public ResponseEntity criarAtualizar(CorreiosFranquiaDTO correiosFranquiaDTO){
        try{
            return ResponseEntity.ok().body(this.correiosFranquiaContextService.updateOrSave(correiosFranquiaDTO));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity getForFranquia(String franquiaId){
        try{
            return ResponseEntity.ok().body(this.correiosFranquiaContextService.getForFranquia(franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
