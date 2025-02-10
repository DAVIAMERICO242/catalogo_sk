package com.skyler.catalogo.domain.cep;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cep")
public class CEPController {

    private final CepService cepService;

    public CEPController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping
    public ResponseEntity getLocalidade(String cep){
        try{
            return ResponseEntity.ok().body(this.cepService.getLocalidade(cep));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
