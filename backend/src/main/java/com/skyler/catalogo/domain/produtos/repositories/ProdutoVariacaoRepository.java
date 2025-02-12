package com.skyler.catalogo.domain.produtos.repositories;

import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao,String> {

    @Query("SELECT pv FROM ProdutoVariacao pv " +
            "JOIN FETCH pv.produto p " +
            "WHERE pv.systemId IN :ids ")
    List<ProdutoVariacao> findAllById(List<String> ids);
}

