package com.skyler.catalogo.domain.descontos.carrinho.services;

import com.skyler.catalogo.domain.catalogo.ProdutoCatalogo;
import com.skyler.catalogo.domain.catalogo.ProdutoCatalogoRepository;
import com.skyler.catalogo.domain.descontos.carrinho.DTOs.DescontoCarrinhoDTO;
import com.skyler.catalogo.domain.descontos.carrinho.entities.DelimitedTermsFromCartDiscount;
import com.skyler.catalogo.domain.descontos.carrinho.entities.DescontoCarrinho;
import com.skyler.catalogo.domain.descontos.carrinho.entities.ExcludedTermsFromCartDiscount;
import com.skyler.catalogo.domain.descontos.carrinho.repositories.DescontoCarrinhoRepository;
import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DescontoCarrinhoService {

    private final LojaRepository lojaRepository;
    private final ProdutoCatalogoRepository produtoCatalogoRepository;
    private final DescontoCarrinhoRepository descontoCarrinhoRepository;

    public DescontoCarrinhoService(LojaRepository lojaRepository, ProdutoCatalogoRepository produtoCatalogoRepository, DescontoCarrinhoRepository descontoCarrinhoRepository) {
        this.lojaRepository = lojaRepository;
        this.produtoCatalogoRepository = produtoCatalogoRepository;
        this.descontoCarrinhoRepository = descontoCarrinhoRepository;
    }




}
