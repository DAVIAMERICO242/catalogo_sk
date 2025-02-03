package com.skyler.catalogo.domain.banners;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkBannerService {
    private final BannerService bannerService;
    private final BannerRepository bannerRepository;
    private final BannerLojaRepository bannerLojaRepository;

    public BulkBannerService(BannerService bannerService, BannerRepository bannerRepository, BannerLojaRepository bannerLojaRepository) {
        this.bannerService = bannerService;
        this.bannerRepository = bannerRepository;
        this.bannerLojaRepository = bannerLojaRepository;
    }

    public void bulkdBanners(List<BannerRequest> banners) throws Exception {
        for(BannerRequest bannersRequest:banners){
            this.bannerRepository.deleteAllBannersForLojaIdAndIndex(bannersRequest.getLojaInfo().get(0).getSystemId(),bannersRequest.getLojaInfo().get(0).getIndex());
            this.bannerService.postOrReindexBanner(bannersRequest);
        }
    }
}
