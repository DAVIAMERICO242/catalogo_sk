package com.skyler.catalogo.domain.descontos.carrinho.repositories;

import com.skyler.catalogo.domain.descontos.carrinho.entities.DescontoCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DescontoCarrinhoRepository extends JpaRepository<DescontoCarrinho,String> {

    @Query("SELECT dc FROM DescontoCarrinho dc " +
            "LEFT JOIN FETCH dc.loja l " +
            "LEFT JOIN FETCH dc.relatedProduct " +
            "LEFT JOIN FETCH dc.delimitedTerms " +
            "LEFT JOIN FETCH dc.excludedTerms  " +
            "LEFT JOIN FETCH dc.intervalDetails i " +
            "LEFT JOIN FETCH i.productsWithDiscountOnIntervalReach ip " +
            "LEFT JOIN FETCH ip.produtoCatalogo " +
            "WHERE l.systemId = :lojaId ")
    List<DescontoCarrinho> findAllByLojaId(String lojaId);
}
