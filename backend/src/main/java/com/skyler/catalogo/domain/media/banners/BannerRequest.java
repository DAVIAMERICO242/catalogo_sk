package com.skyler.catalogo.domain.media.banners;

import lombok.Data;

import java.util.List;

@Data
public class BannerRequest {
    String lojaId;
    List<Media> media;

    @Data
    public static class Media{
        String bannerFileNameWithExtension;
        Window window;
        String base64;
    }
}
