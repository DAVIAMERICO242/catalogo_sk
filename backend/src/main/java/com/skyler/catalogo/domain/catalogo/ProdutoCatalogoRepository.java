package com.skyler.catalogo.domain.catalogo;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface ProdutoCatalogoRepository extends JpaRepository<ProdutoCatalogo,String> {

    @Query("SELECT pc FROM ProdutoCatalogo pc " +
            "JOIN FETCH pc.loja l " +
            "JOIN FETCH pc.produtoBaseFranquia pb " +
            "JOIN FETCH pb.variacoes " +
            "WHERE l.slug = :slug")
    List<ProdutoCatalogo> findAllByLojaSlug(String slug);


    @Query("SELECT pc FROM ProdutoCatalogo pc " +
            "JOIN FETCH pc.loja l " +
            "JOIN FETCH pc.produtoBaseFranquia pb  " +
            "JOIN FETCH pb.variacoes " +
            "WHERE l.slug = :slug AND pb.systemId = :systemId ")
    Optional<ProdutoCatalogo> findByProdutoIdAndLojaSlug(String systemId, String slug);

    @Override
    @Query("SELECT pc FROM ProdutoCatalogo pc " +
            "JOIN FETCH pc.loja l " +
            "JOIN FETCH pc.produtoBaseFranquia pb  " +
            "JOIN FETCH pb.variacoes " +
            "WHERE pc.systemId = :systemId ")
    Optional<ProdutoCatalogo> findById(@NotNull String systemId);

    @Transactional
    @Modifying
    @Query("DELETE FROM ProdutoCatalogo pc " +
            "WHERE pc.produtoBaseFranquia IN " +
            "(SELECT pb FROM Produto pb " +
            "WHERE pb.systemId = :productId ) " +
            "AND pc.loja IN (SELECT l FROM Loja l WHERE l.slug = :lojaSlug )  ")
    void deleteByProductIdAndLojaSlug(String productId, String lojaSlug);
}
