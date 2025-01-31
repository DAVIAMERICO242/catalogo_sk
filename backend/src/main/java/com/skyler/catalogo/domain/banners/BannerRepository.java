package com.skyler.catalogo.domain.banners;

import org.springframework.boot.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<BannerEnt,String> {
}
