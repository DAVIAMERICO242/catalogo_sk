package com.skyler.catalogo.domain.banners;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name="banners")
@EqualsAndHashCode(of="systemId")
public class BannerEnt {
    @Id
    @Column(name="system_id")
    private String systemId = UUID.randomUUID().toString();
    private String urlDesktop;
    private String urlMobile;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "banner")
    @JsonManagedReference
    private Set<BannerLojas> bannerLojas = new HashSet<>();
}
