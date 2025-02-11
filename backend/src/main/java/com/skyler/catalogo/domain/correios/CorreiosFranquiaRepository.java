package com.skyler.catalogo.domain.correios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.Optional;

public interface CorreiosFranquiaRepository extends JpaRepository<CorreiosFranquiaContext,String> {

    @Query("SELECT cfc FROM CorreiosFranquiaContext cfc " +
            "WHERE cfc.franquia.systemId = :franquiaId")
    Optional<CorreiosFranquiaContext> findByFranquiaId(String franquiaId);
}
