package com.skyler.catalogo.domain.franquias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FranquiaRepository extends JpaRepository<Franquia,String> {

    @Override
    @Query("SELECT f FROM Franquia f " +
            "JOIN FETCH f.lojas")
    List<Franquia> findAll();
}
