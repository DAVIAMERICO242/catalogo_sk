package com.skyler.catalogo.domain.shipping.comprimentoCaixa;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comprimento-caixa")
public class ComprimentoCaixaController {

    private final ComprimentoCaixaService comprimentoCaixaService;

    public ComprimentoCaixaController(ComprimentoCaixaService comprimentoCaixaService) {
        this.comprimentoCaixaService = comprimentoCaixaService;
    }

    @GetMapping
    public ResponseEntity getByFranquiaId(String franquiaId){
        try{
            return ResponseEntity.ok().body(this.comprimentoCaixaService.getByFranquiaId(franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping
    public ResponseEntity saveUpdate(@RequestBody ComprimentoCaixaDTO comprimentoCaixaDTO){
        try{
            return ResponseEntity.ok().body(this.comprimentoCaixaService.updateOrSave(comprimentoCaixaDTO));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
