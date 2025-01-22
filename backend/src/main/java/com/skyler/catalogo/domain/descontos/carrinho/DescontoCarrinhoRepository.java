package com.skyler.catalogo.domain.descontos.carrinho;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DescontoCarrinhoRepository extends JpaRepository<DescontoCarrinho,String> {

    @Query("SELECT dc FROM DescontoCarrinho dc " +
            "LEFT JOIN FETCH dc.bonusOutOfCartCatalogProduct b " +
            "JOIN FETCH dc.loja l " +
            "WHERE l.systemId = :lojaId ")
    List<DescontoCarrinho> findAllByLojaId(String lojaId);
}
