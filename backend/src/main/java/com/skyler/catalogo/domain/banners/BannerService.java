package com.skyler.catalogo.domain.banners;

import com.skyler.catalogo.domain.lojas.Loja;
import com.skyler.catalogo.domain.lojas.LojaRepository;
import com.skyler.catalogo.infra.storage.MinioService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public IdResponse postBanner(BannerRequest bannerRequest) throws Exception {//cadastro e reindex, NÃO É POSSIVEL ATUALIZAR BANNER SEM DELETAR
        BannerEnt bannerEnt = new BannerEnt();
        if(bannerRequest.getSystemId()!=null && !bannerRequest.getSystemId().isBlank()){
            Optional<BannerEnt> bannerEntOptional = this.bannerRepository.findById(bannerRequest.getSystemId());
            if(bannerEntOptional.isPresent()){
                bannerEnt = bannerEntOptional.get();
            }
        }
        String desktopExtension = bannerRequest.getMedia().stream().filter(o->o.getWindow()!=null && o.getBannerExtension()!=null && o.getWindow().equals(Window.DESKTOP)).map(o->o.getBannerExtension()).findFirst().orElse(null);
        String mobileExtension = bannerRequest.getMedia().stream().filter(o->o.getWindow()!=null &&  o.getBannerExtension()!=null &&  o.getWindow().equals(Window.MOBILE)).map(o-> o.getBannerExtension()).findFirst().orElse(null);
        for(BannerRequest.Media media:bannerRequest.getMedia()){
            if(media==null || media.getWindow()==null){
                continue;
            }
            if(media.getWindow().equals(Window.MOBILE) && media.getBase64()!=null && !media.getBase64().isBlank()  && mobileExtension!=null && !mobileExtension.isBlank()){
                bannerEnt.setUrlMobile("https://s3.skyler.com.br/catalogosk/banners/MOBILE/" + bannerEnt.getSystemId() + "." + mobileExtension);
                this.minioService.postBase64(
                        media.getBase64().split(",")[1],
                        bannerEnt.getSystemId() + "." + mobileExtension,
                        "/banners/" + media.getWindow() + "/");
            }
            if(media.getWindow().equals(Window.DESKTOP)  && media.getBase64()!=null && !media.getBase64().isBlank() && desktopExtension!=null && !desktopExtension.isBlank()){
                bannerEnt.setUrlDesktop("https://s3.skyler.com.br/catalogosk/banners/DESKTOP/" + bannerEnt.getSystemId() + "." + desktopExtension);
                this.minioService.postBase64(
                        media.getBase64().split(",")[1],
                        bannerEnt.getSystemId() + "." + desktopExtension,
                        "/banners/" + media.getWindow() + "/");
            }
        }
        Optional<Loja> lojaOptional = this.lojaRepository.findById(bannerRequest.getLojaInfo().getSystemId());
        if(lojaOptional.isPresent()){
            Loja loja = lojaOptional.get();
            bannerEnt.setLoja(loja);
            bannerEnt.setIndexOnStore(bannerRequest.getLojaInfo().getIndex());
        }
        this.bannerRepository.save(bannerEnt);
        return new IdResponse(bannerEnt.getSystemId());
    }

    @Transactional
    public synchronized void saveMadeReindex(List<BannerRequest> bannersReindexedForLoja){//ja foi reindexado no frontend
        List<BannerEnt> bannersEnt = this.bannerRepository.findAllById(bannersReindexedForLoja.stream().map(o->o.getSystemId()).toList());
        for(BannerEnt bannerEnt:bannersEnt){
            Optional<BannerRequest> regardingDTO = bannersReindexedForLoja.stream().filter(o->o.getSystemId()!=null && o.getSystemId().equals(bannerEnt.getSystemId())).findFirst();
            if(regardingDTO.isPresent()){
                bannerEnt.setIndexOnStore(regardingDTO.get().getLojaInfo().getIndex());
            }
        }
        this.bannerRepository.saveAll(bannersEnt);
    }

    @Transactional
    public void deletarBannerDaLoja(String bannerId, Boolean isMobile){//se deletar de uma loja deleta de todas foda,
        BannerEnt bannerEnt = this.bannerRepository.findById(bannerId).get();
        if(isMobile){
            String objectMobile = bannerEnt.getUrlMobile().split("catalogosk/")[1];
            bannerEnt.setUrlMobile(null);
            this.bannerRepository.save(bannerEnt);
            if(bannerEnt.getUrlMobile()==null && bannerEnt.getUrlDesktop()==null){
                this.bannerRepository.deleteById(bannerId);
            }
            this.minioService.removeDirectory(objectMobile);
        }else{
            String objectDesktop = bannerEnt.getUrlDesktop().split("catalogosk/")[1];
            bannerEnt.setUrlDesktop(null);
            this.bannerRepository.save(bannerEnt);
            if(bannerEnt.getUrlMobile()==null && bannerEnt.getUrlDesktop()==null){
                this.bannerRepository.deleteById(bannerId);
            }
            this.minioService.removeDirectory(objectDesktop);
        }
    }

    public List<BannerRequest> getBanners(String lojaId,String franquiaId) throws Exception {
        List<BannerRequest> output = new ArrayList<>();
        List<BannerEnt> bannersEnt = null;
        if(lojaId!=null && !lojaId.isBlank()){
            bannersEnt = this.bannerRepository.findAllByLojaId(lojaId);
        }else{
            bannersEnt = this.bannerRepository.findAllByFranquiaId(franquiaId);
        }
        for(BannerEnt bannerEnt:bannersEnt){
            BannerRequest bannerRequest = new BannerRequest();
            bannerRequest.setSystemId(bannerEnt.getSystemId());
            BannerRequest.Media desktop = new BannerRequest.Media();
            if(bannerEnt.getUrlDesktop()!=null){
                desktop.setWindow(Window.DESKTOP);
                desktop.setBannerUrl(bannerEnt.getUrlDesktop());
            }
            bannerRequest.addMediaInfo(desktop);
            BannerRequest.Media mobile = new BannerRequest.Media();
            if(bannerEnt.getUrlMobile()!=null){
                mobile.setWindow(Window.MOBILE);
                mobile.setBannerUrl(bannerEnt.getUrlMobile());
            }
            bannerRequest.addMediaInfo(mobile);
            BannerRequest.LojaInfo lojaInfo = new BannerRequest.LojaInfo();
            lojaInfo.setSystemId(bannerEnt.getLoja().getSystemId());
            lojaInfo.setIndex(bannerEnt.getIndexOnStore());
            bannerRequest.setLojaInfo(lojaInfo);
            output.add(bannerRequest);
        }
        return output;
    }


}
