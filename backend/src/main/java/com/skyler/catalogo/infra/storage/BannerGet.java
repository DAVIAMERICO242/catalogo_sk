package com.skyler.catalogo.infra.storage;

import lombok.Data;

@Data
public class BannerGet {
    String lojaId;
    Integer index;
    String bannerDesktop;
    String bannerMobile;

}
