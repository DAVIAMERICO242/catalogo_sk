package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import org.springframework.stereotype.Service;

@Service
public class DescontoCarrinhoService {

    private final LojaRepository lojaRepository;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;

    public DescontoCarrinhoService(LojaRepository lojaRepository, ProdutoCatalogoRepository produtoCatalogoRepository) {
        this.lojaRepository = lojaRepository;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
    }




}
