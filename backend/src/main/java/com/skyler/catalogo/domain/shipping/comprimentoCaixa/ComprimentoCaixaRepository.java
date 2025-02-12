package com.skyler.catalogo.domain.shipping.comprimentoCaixa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ComprimentoCaixaRepository extends JpaRepository<ComprimentoCaixa,String> {

    @Query("SELECT cc FROM ComprimentoCaixa cc " +
            "WHERE cc.franquia.systemId = :franquiaId")
    Optional<ComprimentoCaixa> findByFranquiaId(String franquiaId);


}
