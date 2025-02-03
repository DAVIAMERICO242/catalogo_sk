package com.skyler.catalogo.domain.banners;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.skyler.catalogo.domain.lojas.Loja;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.annotation.Lazy;

import java.lang.module.FindException;
import java.util.UUID;

@Table(name="banners_lojas")
@Entity
@Data
@EqualsAndHashCode(of="systemId")
public class BannerLojas {//no modelo um banner pode ter varias lojas, mas isso nao funciona, nao mude o modelo pq pode quebrar
    @Id
    private String systemId = UUID.randomUUID().toString();
    private Integer indexOnStore;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="loja_id")
    @JsonBackReference
    private Loja loja;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="banner_id")
    @JsonBackReference
    private BannerEnt banner;
}
