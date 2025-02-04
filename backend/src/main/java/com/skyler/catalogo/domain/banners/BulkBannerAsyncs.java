package com.skyler.catalogo.domain.banners;

import com.skyler.catalogo.infra.storage.MinioService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkBannerAsyncs {

    private final BannerRepository bannerRepository;
    private final MinioService minioService;

    public BulkBannerAsyncs(BannerRepository bannerRepository, MinioService minioService) {
        this.bannerRepository = bannerRepository;
        this.minioService = minioService;
    }

    @Async
    public void deleteBannerFromMedia(BannerEnt bannerEnt){
        if(bannerEnt.getUrlMobile()!=null){
            String objectMobile = bannerEnt.getUrlMobile().split("catalogosk/")[1];
            this.minioService.removeDirectory(objectMobile);
        }
        if(bannerEnt.getUrlDesktop()!=null){
            String objectDesktop = bannerEnt.getUrlDesktop().split("catalogosk/")[1];
            this.minioService.removeDirectory(objectDesktop);
        }
    }
}
