package com.skyler.catalogo.domain.banners;

import com.skyler.catalogo.infra.storage.BannerGet;
import com.skyler.catalogo.infra.storage.MinioService;
import jakarta.transaction.Transactional;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BannerService {

    private final MinioService minioService;
    private final BannerRepository bannerRepository;

    public BannerService(MinioService minioService, BannerRepository bannerRepository) {
        this.minioService = minioService;
        this.bannerRepository = bannerRepository;
    }

    @Transactional
    public void postBanner(BannerRequest bannerRequest) throws Exception {
        BannerEnt bannerEnt = new BannerEnt();
        String desktopExtension = bannerRequest.getMedia().stream().filter(o->o.getWindow().equals(Window.DESKTOP)).map(o->o.getBannerExtension()).findFirst().get();
        String mobileExtension = bannerRequest.getMedia().stream().filter(o->o.getWindow().equals(Window.MOBILE)).map(o->o.getBannerExtension()).findFirst().get();
        bannerEnt.setUrlDesktop("https://s3.skyler.com.br/catalogosk/banners/DESKTOP/" + bannerEnt.getSystemId() + desktopExtension);
        bannerEnt.setUrlMobile("https://s3.skyler.com.br/catalogosk/banners/MOBILE/" + bannerEnt.getSystemId() + mobileExtension);
        for(BannerRequest.Media media:bannerRequest.getMedia()){
            if(media.getWindow().equals(Window.MOBILE)){
                this.minioService.postBase64(
                        media.getBase64(),
                        bannerEnt.getSystemId() + mobileExtension,
                        "/banners/" + media.getWindow());
            }
            if(media.getWindow().equals(Window.DESKTOP)){
                this.minioService.postBase64(
                        media.getBase64(),
                        bannerEnt.getSystemId() + desktopExtension,
                        "/banners/" + media.getWindow());
            }
        }
        this.bannerRepository.save(bannerEnt);
    }

    public List<BannerGet> getBanners(String lojaId) throws Exception {
        List<BannerGet> output = new ArrayList<>();
        for(int i=0;i<5;i++){
            BannerGet row = new BannerGet();
            List<String> urlsMobile = this.minioService.getFileUrlsFromPath(
                    "lojas/" + lojaId + "/banners/" + i + "/" + Window.MOBILE
            );
            List<String> urlsDesktop = this.minioService.getFileUrlsFromPath(
                    "lojas/" + lojaId + "/banners/" + i + "/" + Window.DESKTOP
            );
            if(!urlsMobile.isEmpty()){
                row.setLojaId(lojaId);
                row.setIndex(i);
                row.setBannerMobile(urlsMobile.get(0));
            }
            if(!urlsDesktop.isEmpty()){
                row.setLojaId(lojaId);
                row.setIndex(i);
                row.setBannerDesktop(urlsDesktop.get(0));
            }
            output.add(row);
        }
        return output;
    }


}
