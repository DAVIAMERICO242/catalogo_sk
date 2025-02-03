package com.skyler.catalogo.domain.banners;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media/banner")
public class BannerController {

    private final BannerService bannerService;

    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @PostMapping
    public ResponseEntity banner(@RequestBody  BannerRequest bannerRequest){
        try{
            IdResponse id = this.bannerService.postOrReindexBanner(bannerRequest);
            return ResponseEntity.ok().body(id);
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity getBanners(@RequestParam(required = false) String lojaId, @RequestParam(required = true) String franquiaId){
        try{
            return ResponseEntity.ok().body(this.bannerService.getBanners(lojaId,franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity desassociarBanner(String bannerId,String lojaId, Boolean isMobile){
        try{
            this.bannerService.deletarBannerDaLoja(bannerId,lojaId,isMobile);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
