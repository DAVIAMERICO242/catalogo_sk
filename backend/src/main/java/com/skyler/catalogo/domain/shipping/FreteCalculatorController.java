package com.skyler.catalogo.domain.shipping;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipping-calculator")
public class FreteCalculatorController {

    private final ShippingCalculator shippingCalculator;

    public FreteCalculatorController(ShippingCalculator shippingCalculator) {
        this.shippingCalculator = shippingCalculator;
    }

    @PostMapping("/how-should-be-calculated")
    public ResponseEntity how(@RequestBody ShippingCalculationRequest shippingCalculationRequest){
        try{
            return ResponseEntity.ok().body(this.shippingCalculator.getHowShouldBeCalculated(shippingCalculationRequest));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }


    @PostMapping
    public ResponseEntity calculateFrete(@RequestBody ShippingCalculationRequest shippingCalculationRequest){
        try{
            return ResponseEntity.ok().body(this.shippingCalculator.getFrete(shippingCalculationRequest));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
