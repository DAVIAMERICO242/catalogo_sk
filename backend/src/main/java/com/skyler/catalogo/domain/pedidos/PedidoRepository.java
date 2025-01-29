package com.skyler.catalogo.domain.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido,String> {

    @Query("SELECT p FROM Pedido p " +
            "JOIN FETCH p.loja  " +
            "JOIN FETCH p.produtos " +
            "LEFT JOIN FETCH p.descontosAplicados da " +
            "LEFT JOIN FETCH da.desconto d " +
            "LEFT JOIN FETCH d.descontoFrete " +
            "LEFT JOIN FETCH d.descontoGenericoCarrinho " +
            "LEFT JOIN FETCH d.descontoMaiorValor " +
            "LEFT JOIN FETCH d.descontoMenorValor " +
            "LEFT JOIN FETCH d.descontoProgressivo " +
            "LEFT JOIN FETCH d.descontoSimplesProduto " +
            "LEFT JOIN FETCH d.descontoSimplesTermo " +
            "LEFT JOIN FETCH d.delimitedTermos " +
            "LEFT JOIN FETCH d.excludedTermos " +
            "WHERE p.loja.systemId = :lojaId ")
    List<Pedido> findAllByLojaId(String lojaId);
}
