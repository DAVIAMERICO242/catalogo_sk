package com.skyler.catalogo.domain.banners;

import org.springframework.boot.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<BannerEnt,String> {
    @Query("SELECT b From BannerEnt b " +
            "JOIN FETCH b.bannerLojas bl " +
            "JOIN FETCH bl.loja l  " +
            "WHERE b.systemId = :id")
    Optional<BannerEnt> findById(String id);



    @Query("SELECT b From BannerEnt b " +
            "JOIN FETCH b.bannerLojas bl " +
            "JOIN FETCH bl.loja l  ")
    List<BannerEnt> findAll();

    @Query("SELECT b From BannerEnt b " +
            "JOIN FETCH b.bannerLojas bl " +
            "JOIN FETCH bl.loja l " +
            "WHERE l.franquia.systemId = :id  ")
    List<BannerEnt> findAllByFranquiaId(String id);

    @Query("SELECT b From BannerEnt b " +
            "JOIN FETCH b.bannerLojas bl " +
            "JOIN FETCH bl.loja l " +
            "WHERE l.systemId = :id  ")
    List<BannerEnt> findAllByLojaId(String id);
}
