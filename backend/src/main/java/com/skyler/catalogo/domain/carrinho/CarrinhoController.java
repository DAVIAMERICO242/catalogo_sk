package com.skyler.catalogo.domain.carrinho;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @PostMapping("/descontos-validos")
    public ResponseEntity calcularDescontos(@RequestBody CarrinhoRequest carrinhoRequest){
        try{
            return ResponseEntity.ok().body(this.carrinhoService.getDescontos(carrinhoRequest));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
