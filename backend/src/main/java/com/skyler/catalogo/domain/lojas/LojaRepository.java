package com.skyler.catalogo.domain.lojas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LojaRepository extends JpaRepository<Loja,String> {

    @Override
    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE (NOT l.nome LIKE '%CD%' AND NOT l.nome LIKE '%DASLEN%' AND NOT l.nome LIKE '%SITE%' AND NOT l.nome LIKE '%DIGITAL%' ) " +
            "ORDER BY l.nome ASC ")
    List<Loja> findAll();

    @Query("SELECT l FROM Loja l " +
            "JOIN FETCH l.franquia " +
            "WHERE l.integradorId = :id ")
    Optional<Loja> findByIntegradorId(String id);
}
