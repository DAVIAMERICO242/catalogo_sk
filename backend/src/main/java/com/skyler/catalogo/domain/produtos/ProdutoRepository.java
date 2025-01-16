package com.skyler.catalogo.domain.produtos;

import com.skyler.catalogo.domain.franquias.Franquia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto,String> {

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH p.franquia f " +
            "WHERE p.sku = :sku AND f = :franquia ")
    Optional<Produto> findBySkuAndFranquia(Franquia franquia, String sku);

    @Override
    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v ")
    List<Produto> findAll();

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH franquia f " +
            "ORDER BY p.sku ")
    List<Produto> loadAllWithFranquiaAndVariacoes();
}
