package com.skyler.catalogo.domain.franquias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FranquiaRepository extends JpaRepository<Franquia,String> {

    @Override
    @Query("SELECT f FROM Franquia f " +
            "JOIN FETCH f.lojas")
    List<Franquia> findAll();

    @Query("SELECT f FROM Franquia f " +
            "JOIN FETCH f.lojas " +
            "WHERE f.integradorId = :id ")
    Optional<Franquia> findByIntegradorId(String id);
}
