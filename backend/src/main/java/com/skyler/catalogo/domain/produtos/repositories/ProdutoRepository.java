package com.skyler.catalogo.domain.produtos.repositories;

import com.skyler.catalogo.domain.franquias.Franquia;
import com.skyler.catalogo.domain.produtos.entities.Produto;
import com.skyler.catalogo.domain.produtos.entities.ProdutoVariacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto,String>, JpaSpecificationExecutor<Produto> {

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


    @Query("SELECT p.systemId " +
            "FROM Produto p " +
            "WHERE p.franquia = :franquia")
    Page<Long> pagedIds(PageRequest pageRequest,Franquia franquia);

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH p.franquia f " +
            "WHERE p.systemId IN :ids ")
    List<Produto> findAllByIdIn(List<Long> ids);

    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.variacoes v " +
            "JOIN FETCH p.franquia f " +
            "WHERE f = :franquia ")
    List<Produto> findAllByFranquia(Franquia franquia);


    @Query("SELECT p FROM Produto p " +
            "JOIN FETCH p.franquia f " +
            "WHERE f = :franquia ")
    Page<Produto> findAllPagedByFranquiaWithoutVariacoes(PageRequest pageRequest,Franquia franquia);


    @Query("SELECT p.variacoes FROM Produto p " +
            "WHERE p.systemId = :productSystemId")
    List<ProdutoVariacao> findVariacoesForProduct(String productSystemId);//o id do produto ja abstrai a franquia e o sku

    @Query("SELECT p FROM Produto p " +
            "WHERE p.franquia = :franquia ")
    Page<Produto> findAllByFranquiaPaged(PageRequest pageRequest,Franquia franquia);


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
