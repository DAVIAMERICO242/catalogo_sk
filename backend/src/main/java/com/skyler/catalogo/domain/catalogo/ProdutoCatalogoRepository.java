package com.skyler.catalogo.domain.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoCatalogoRepository extends JpaRepository<ProdutoCatalogo,String> {

    @Query("SELECT pc FROM ProdutoCatalogo pc " +
            "JOIN FETCH pc.loja l " +
            "JOIN FETCH pc.produtoBaseFranquia pb " +
            "WHERE l.slug = :slug")
    List<ProdutoCatalogo> findAllByLojaSlug(String slug);
}
