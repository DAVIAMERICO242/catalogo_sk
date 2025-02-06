package com.skyler.catalogo.domain.carrinho;

import com.skyler.catalogo.domain.descontos.DTOs.DescontoAplicadoDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarrinhoService {

    public List<DescontoAplicadoDTO> getDescontos(CarrinhoRequest carrinhoRequest){
        List<DescontoAplicadoDTO> descontoAplicadoDTOS = new ArrayList<>();

        return descontoAplicadoDTOS;
    }
}
