package com.skyler.catalogo.domain.carrinho;

import com.skyler.catalogo.domain.descontos.DTOs.DescontoAplicadoDTO;
import com.skyler.catalogo.domain.descontos.services.DiscountCalculator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarrinhoService {

    private final DiscountCalculator discountCalculator;

    public CarrinhoService(DiscountCalculator discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    public List<DescontoAplicadoDTO> getDescontos(CarrinhoRequest carrinhoRequest){
        return this.discountCalculator.processChainForCurrentEpochAndDiscountable(carrinhoRequest).getDescontos();
    }
}
