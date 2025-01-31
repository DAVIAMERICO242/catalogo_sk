package com.skyler.catalogo.domain.banners;

import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.infra.storage.BannerGet;
import com.skyler.catalogo.infra.storage.MinioService;
import jakarta.transaction.Transactional;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannerService {

    private final MinioService minioService;
    private final BannerRepository bannerRepository;
    private final LojaRepository lojaRepository;

    public BannerService(MinioService minioService, BannerRepository bannerRepository, LojaRepository lojaRepository) {
        this.minioService = minioService;
        this.bannerRepository = bannerRepository;
        this.lojaRepository = lojaRepository;
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
        for(BannerRequest.LojaInfo lojaInfo:bannerRequest.getLojaInfo()){
            BannerLojas bannerLoja = new BannerLojas();
            bannerLoja.setIndexOnStore(lojaInfo.getIndex());
            bannerLoja.setLoja(this.lojaRepository.findById(lojaInfo.getSystemId()).get());
            bannerEnt.addRelacaoLoja(bannerLoja);
        }
        this.bannerRepository.save(bannerEnt);
    }

    @Transactional
    public void deletarBannerDaLoja(String bannerId, String lojaId){//se deletar de uma loja deleta de todas foda,
        BannerEnt bannerEnt = this.bannerRepository.findById(bannerId).get();
        bannerEnt.setBannerLojas(bannerEnt.getBannerLojas().stream().filter(o->!o.getLoja().getSystemId().equals(lojaId)).collect(Collectors.toSet()));
        this.bannerRepository.save(bannerEnt);
        if(bannerEnt.getBannerLojas().isEmpty()){//e é o ultimo banner da loja
            String objectDesktop = bannerEnt.getUrlDesktop().split("catalogosk/")[1];
            String objectMobile = bannerEnt.getUrlMobile().split("catalogosk/")[1];
            this.minioService.removeDirectory(objectDesktop);
            this.minioService.removeDirectory(objectMobile);
            this.bannerRepository.deleteById(bannerId);
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
