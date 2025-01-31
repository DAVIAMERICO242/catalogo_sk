package com.skyler.catalogo.domain.banners;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BannerLojaRepository extends JpaRepository<BannerLojas,String> {

}
