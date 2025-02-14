package com.skyler.catalogo.infra.integrador;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/integrador")
public class IntegradorController {

    private final Integrador integrador;

    public IntegradorController(Integrador integrador) {
        this.integrador = integrador;
    }

    @PostMapping
    public ResponseEntity integrar(String senha){
        try{
            if(!senha.equals("896321574")){
                return ResponseEntity.status(400).body("SENHA INVÁLIDA");
            }
            new Thread(()->{this.integrador.updateCron();}).start();
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
