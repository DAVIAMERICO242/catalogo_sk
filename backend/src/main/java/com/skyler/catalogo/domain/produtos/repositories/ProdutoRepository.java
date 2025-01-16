package com.skyler.catalogo.domain.produtos.repositories;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.produtos.entities.Produto;
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

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH p.franquia f " +
            "WHERE p.produtoIntegradorId = :integradorId ")
    Optional<Produto> findByIntegradorId(String integradorId);

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH p.franquia f " +
            "WHERE f = :franquia ")
    List<Produto> findAllByFranquia(Franquia franquia);

    @Override
    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v ")
    List<Produto> findAll();

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH franquia f " +
            "ORDER BY p.sku ")
    List<Produto> loadAllWithFranquiaAndVariacoes();

    @Query("SELECT DISTINCT(p.categoria) FROM Produto p " +
            "JOIN p.franquia f " +
            "WHERE f = :franquia")
    List<String> getCategorias(Franquia franquia);

    @Query("SELECT DISTINCT(p.modelagem) FROM Produto p " +
            "JOIN p.franquia f " +
            "WHERE f = :franquia")
    List<String> getModelagens(Franquia franquia);

    @Query("SELECT DISTINCT(p.grupo) FROM Produto p " +
            "JOIN p.franquia f " +
            "WHERE f = :franquia")
    List<String> getGrupos(Franquia franquia);


}
