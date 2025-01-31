package com.skyler.catalogo.domain.lojas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LojaRepository extends JpaRepository<Loja,String> {

    @Override
    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE (NOT  l.nome LIKE '%CD%' AND NOT l.nome LIKE '%MULTIMARCAS' AND NOT l.nome LIKE '%SKYLER FRANCHISING%' AND NOT l.nome LIKE '%Empreendimentos%' AND NOT l.nome LIKE '%Fran' AND NOT l.nome LIKE '%DASLEN%' AND NOT l.nome LIKE '%SITE%' AND NOT l.nome LIKE '%DIGITAL%' ) " +
            "ORDER BY l.nome ASC ")
    List<Loja> findAll();

    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE l.franquia.systemId = :franquiaId AND (NOT  l.nome LIKE '%CD%' AND NOT l.nome LIKE '%MULTIMARCAS' AND NOT l.nome LIKE '%SKYLER FRANCHISING%' AND NOT l.nome LIKE '%Empreendimentos%' AND NOT l.nome LIKE '%Fran' AND NOT l.nome LIKE '%DASLEN%' AND NOT l.nome LIKE '%SITE%' AND NOT l.nome LIKE '%DIGITAL%' ) " +
            "ORDER BY l.nome ASC ")
    List<Loja> findAllByFranquiaId(String franquiaId);


    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE (NOT  l.nome LIKE '%CD%' AND NOT l.nome LIKE '%MULTIMARCAS' AND NOT l.nome LIKE '%SKYLER FRANCHISING%' AND NOT l.nome LIKE '%Empreendimentos%' AND NOT l.nome LIKE '%Fran' AND NOT l.nome LIKE '%DASLEN%' AND NOT l.nome LIKE '%SITE%' AND NOT l.nome LIKE '%DIGITAL%' ) " +
            "AND l.franquia.isMatriz = true " +
            "ORDER BY l.nome ASC ")
    List<Loja> findAllMatriz();

    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE (NOT  l.nome LIKE '%CD%' AND NOT l.nome LIKE '%MULTIMARCAS' AND NOT l.nome LIKE '%SKYLER FRANCHISING%' AND NOT l.nome LIKE '%Empreendimentos%' AND NOT l.nome LIKE '%Fran' AND NOT l.nome LIKE '%DASLEN%' AND NOT l.nome LIKE '%SITE%' AND NOT l.nome LIKE '%DIGITAL%' ) " +
            "AND l.franquia.isMatriz = false " +
            "ORDER BY l.nome ASC ")
    List<Loja> findAllFranquia();


    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE l.integradorId = :id ")
    Optional<Loja> findByIntegradorId(String id);

    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE l.slug = :slug ")
    Optional<Loja> findByLojaSlug(String slug);
}
