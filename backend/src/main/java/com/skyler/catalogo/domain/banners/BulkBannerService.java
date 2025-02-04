package com.skyler.catalogo.domain.banners;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulkBannerService {
    private final BannerService bannerService;
    private final BannerRepository bannerRepository;
    private final BulkBannerAsyncs bulkBannerAsyncs;

    public BulkBannerService(BannerService bannerService, BannerRepository bannerRepository, BulkBannerAsyncs bulkBannerAsyncs) {
        this.bannerService = bannerService;
        this.bannerRepository = bannerRepository;
        this.bulkBannerAsyncs = bulkBannerAsyncs;
    }


    public void bulkdBanners(List<BannerRequest> banners) throws Exception {
        for(BannerRequest bannersRequest:banners){
            this.bannerRepository.deleteAllBannersForLojaIdAndIndex(bannersRequest.getLojaInfo().getSystemId(),bannersRequest.getLojaInfo().getIndex());
            this.bannerService.postBanner(bannersRequest);
        }
    }

    public void bulkDelete(List<String> ids){
        List<BannerEnt> banners = this.bannerRepository.findAllById(ids);
        for(BannerEnt banner:banners){
            this.bulkBannerAsyncs.deleteBannerFromMedia(banner);
        }
        this.bannerRepository.deleteAllById(ids);
    }
}
