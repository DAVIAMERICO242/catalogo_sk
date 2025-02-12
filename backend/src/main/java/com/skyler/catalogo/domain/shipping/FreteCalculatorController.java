package com.skyler.catalogo.domain.shipping;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping-calculator")
public class FreteCalculatorController {

    private final ShippingCalculator shippingCalculator;

    public FreteCalculatorController(ShippingCalculator shippingCalculator) {
        this.shippingCalculator = shippingCalculator;
    }

    @GetMapping("/how-should-be-calculated")
    public ResponseEntity how(@RequestBody ShippingCalculationRequest shippingCalculationRequest){
        try{
            return ResponseEntity.ok().body(this.shippingCalculator.getHowShouldBeCalculated(shippingCalculationRequest));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }


    @GetMapping
    public ResponseEntity calculateFrete(@RequestBody ShippingCalculationRequest shippingCalculationRequest){
        try{
            return ResponseEntity.ok().body(this.shippingCalculator.getFrete(shippingCalculationRequest));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
