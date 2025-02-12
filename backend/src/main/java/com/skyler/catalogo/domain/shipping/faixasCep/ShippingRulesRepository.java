package com.skyler.catalogo.domain.shipping.faixasCep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShippingRulesRepository extends JpaRepository<ShippingRules,String> {

    @Query("SELECT sl FROM ShippingRules sl " +
            "WHERE sl.franquia.systemId = :franquiaId")
    List<ShippingRules> findAllByFranquiaId(String franquiaId);
}
