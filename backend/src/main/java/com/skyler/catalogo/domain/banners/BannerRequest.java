package com.skyler.catalogo.domain.banners;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BannerRequest {

    List<LojaInfo> lojaInfo;
    List<Media> media = new ArrayList<>();//tamanho 2

    @Data
    public static class LojaInfo{//index do banner atual na loja com esse systemId
        String systemId;
        Integer index;
    }

    @Data
    public static class Media{
        String bannerExtension;
        Window window;
        String base64;
    }
}
