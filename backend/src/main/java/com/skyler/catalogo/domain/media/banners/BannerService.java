package com.skyler.catalogo.domain.media.banners;

import com.skyler.catalogo.infra.storage.BannerGet;
import com.skyler.catalogo.infra.storage.MinioService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                    "lojas/"+bannerRequest.getLojaId()+"/banners/" + bannerRequest.getIndex() + "/" + media.getWindow() + "/" + media.getBannerFileNameWithExtension()
            );
        }
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
