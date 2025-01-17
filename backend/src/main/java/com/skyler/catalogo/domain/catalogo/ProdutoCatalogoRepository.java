package com.skyler.catalogo.domain.catalogo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoCatalogoRepository extends JpaRepository<ProdutoCatalogo,String> {

    @Query("SELECT pc FROM ProdutoCatalogo pc " +
            "JOIN FETCH pc.loja l " +
            "JOIN FETCH pc.produtoBaseFranquia pb " +
            "WHERE l.slug = :slug")
    List<ProdutoCatalogo> findAllByLojaSlug(String slug);


    @Query("SELECT pc FROM ProdutoCatalogo pc " +
            "JOIN FETCH pc.loja l " +
            "JOIN FETCH pc.produtoBaseFranquia pb " +
            "WHERE l.slug = :slug AND pb.systemId = :systemId ")
    Optional<ProdutoCatalogo> findByProdutoIdAndLojaSlug(String systemId, String slug);
}
