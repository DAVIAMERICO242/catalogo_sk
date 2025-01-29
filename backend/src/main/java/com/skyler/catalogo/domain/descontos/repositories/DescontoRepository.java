package com.skyler.catalogo.domain.descontos.repositories;

import com.skyler.catalogo.domain.descontos.entities.Desconto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface DescontoRepository extends JpaRepository<Desconto,String> {

    @Query("SELECT COUNT(da) FROM DescontosAplicados da " +
            "WHERE da.desconto.systemId = :id")
    int numeroAplicacoes(String id);

    @Query("SELECT d FROM Desconto d " +
            "LEFT JOIN FETCH d.loja l " +
            "LEFT JOIN FETCH d.descontoFrete df " +
            "LEFT JOIN FETCH d.descontoGenericoCarrinho dgc " +
            "LEFT JOIN FETCH d.descontoMaiorValor dmav " +
            "LEFT JOIN FETCH d.descontoMenorValor dmev " +
            "LEFT JOIN FETCH d.descontoProgressivo dp " +
            "LEFT JOIN FETCH dp.intervalos " +
            "LEFT JOIN FETCH d.descontoSimplesProduto dsp " +
            "LEFT JOIN FETCH dsp.produtoCatalogo pc " +
            "LEFT JOIN FETCH pc.produtoBaseFranquia pbf " +
            "LEFT JOIN FETCH d.descontoSimplesTermo dst " +
            "LEFT JOIN FETCH d.delimitedTermos dt " +
            "LEFT JOIN FETCH d.excludedTermos et " +
            "WHERE d.loja.systemId = :lojaId  ")
    List<Desconto> findAllByLojaId(String lojaId);

    @Query("SELECT d FROM Desconto d " +
            "LEFT JOIN FETCH d.loja l " +
            "LEFT JOIN FETCH d.descontoFrete df " +
            "LEFT JOIN FETCH d.descontoGenericoCarrinho dgc " +
            "LEFT JOIN FETCH d.descontoMaiorValor dmav " +
            "LEFT JOIN FETCH d.descontoMenorValor dmev " +
            "LEFT JOIN FETCH d.descontoProgressivo dp " +
            "LEFT JOIN FETCH dp.intervalos " +
            "LEFT JOIN FETCH d.descontoSimplesProduto dsp " +
            "LEFT JOIN FETCH dsp.produtoCatalogo pc " +
            "LEFT JOIN FETCH pc.produtoBaseFranquia pbf " +
            "LEFT JOIN FETCH d.descontoSimplesTermo dst " +
            "LEFT JOIN FETCH d.delimitedTermos dt " +
            "LEFT JOIN FETCH d.excludedTermos et " +
            "WHERE d.loja.systemId = :lojaId AND d.isActive = true AND d.expiresAt >= :today  ")
    List<Desconto> findAllActiveAndNotExpiredByLojaId(LocalDate today,String lojaId);

    @Override
    @Query("SELECT d FROM Desconto d " +
            "LEFT JOIN FETCH d.loja l " +
            "LEFT JOIN FETCH d.descontoFrete df " +
            "LEFT JOIN FETCH d.descontoGenericoCarrinho dgc " +
            "LEFT JOIN FETCH d.descontoMaiorValor dmav " +
            "LEFT JOIN FETCH d.descontoMenorValor dmev " +
            "LEFT JOIN FETCH d.descontoProgressivo dp " +
            "LEFT JOIN FETCH dp.intervalos " +
            "LEFT JOIN FETCH d.descontoSimplesProduto dsp " +
            "LEFT JOIN FETCH dsp.produtoCatalogo pc " +
            "LEFT JOIN FETCH pc.produtoBaseFranquia pbf " +
            "LEFT JOIN FETCH d.descontoSimplesTermo dst " +
            "LEFT JOIN FETCH d.delimitedTermos dt " +
            "LEFT JOIN FETCH d.excludedTermos et " +
            "WHERE d.loja.systemId = :lojaId  ")
    List<Desconto> findAll();

    @Transactional
    @Modifying
    @Query("UPDATE Desconto d " +
            "SET d.isActive = false " +
            "WHERE d.expiresAt <= :now ")
    void updateExpirados(LocalDate now);
}
