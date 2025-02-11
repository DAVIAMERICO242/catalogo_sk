package com.skyler.catalogo.domain.pesoCategorias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PesoCategoriasRepository extends JpaRepository<PesoCategorias,String> {

    @Query("SELECT pc FROM PesoCategorias pc " +
            "JOIN FETCH pc.franquia f " +
            "WHERE f.systemId = :franquiaId")
    List<PesoCategorias> findAllByFranquiaId(String franquiaId);

    @Query("SELECT pc FROM PesoCategorias pc " +
            "JOIN FETCH pc.franquia f " +
            "WHERE f.systemId = :franquiaId AND pc.categoria = :categoria")
    Optional<PesoCategorias> findByCategoriaAndFranquiaId(String categoria,String franquiaId);
}
