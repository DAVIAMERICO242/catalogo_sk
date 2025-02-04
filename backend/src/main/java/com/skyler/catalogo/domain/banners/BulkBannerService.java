package com.skyler.catalogo.domain.banners;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkBannerService {
    private final BannerService bannerService;
    private final BannerRepository bannerRepository;

    public BulkBannerService(BannerService bannerService, BannerRepository bannerRepository) {
        this.bannerService = bannerService;
        this.bannerRepository = bannerRepository;
    }


    public void bulkdBanners(List<BannerRequest> banners) throws Exception {
        for(BannerRequest bannersRequest:banners){
            this.bannerRepository.deleteAllBannersForLojaIdAndIndex(bannersRequest.getLojaInfo().getSystemId(),bannersRequest.getLojaInfo().getIndex());
            this.bannerService.postBanner(bannersRequest);
        }
    }
}
