package com.skyler.catalogo.domain.media.banners;

import com.skyler.catalogo.infra.storage.MinioService;
import org.springframework.stereotype.Service;

@Service
public class BannerService {

    private final MinioService minioService;

    public BannerService(MinioService minioService) {
        this.minioService = minioService;
    }

    public void postBanner(BannerRequest bannerRequest) throws Exception {
        for(BannerRequest.Media media:bannerRequest.getMedia()){
            this.minioService.postBase64(
                    media.getBase64(),
                    media.getBannerFileNameWithExtension(),
                    "lojas/"+bannerRequest.getLojaId()+"/banners/" + media.getWindow() + "/" + media.getBannerFileNameWithExtension()
            );
        }
    }
}
