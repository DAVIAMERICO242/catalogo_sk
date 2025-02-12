package com.skyler.catalogo.domain.shipping.faixasCep;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faixas-cep")
public class ShippingRulesController {

    private final ShippingRulesService shippingRulesService;


    public ShippingRulesController(ShippingRulesService shippingRulesService) {
        this.shippingRulesService = shippingRulesService;
    }

    @PostMapping
    public ResponseEntity postFaixa(@RequestBody ShippingRuleDTO shippingRuleDTO){
        try{
            return ResponseEntity.ok().body(this.shippingRulesService.updateOrSave(shippingRuleDTO));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity getFaixas(String franquiaId){
        try{
            return ResponseEntity.ok().body(this.shippingRulesService.getRules(franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity deletarFaixa(String id){
        try{
            this.shippingRulesService.deletarFaixa(id);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
