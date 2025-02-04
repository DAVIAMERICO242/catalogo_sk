package com.skyler.catalogo.domain.banners;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/media/banner")
public class BannerController {

    private final BannerService bannerService;
    private final BulkBannerService bulkBannerService;

    public BannerController(BannerService bannerService, BulkBannerService bulkBannerService) {
        this.bannerService = bannerService;
        this.bulkBannerService = bulkBannerService;
    }

    @PostMapping
    public ResponseEntity banner(@RequestBody  BannerRequest bannerRequest){
        try{
            IdResponse id = this.bannerService.postBanner(bannerRequest);
            return ResponseEntity.ok().body(id);
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity bannerBulk(@RequestBody List<BannerRequest> banners){
        try{
            this.bulkBannerService.bulkdBanners(banners);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @PostMapping("/save-reindex")
    public ResponseEntity saveReindex(@RequestBody List<BannerRequest> bannersReindexedForLoja){
        try{
            this.bannerService.saveMadeReindex(bannersReindexedForLoja);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @GetMapping
    public ResponseEntity getBanners(@RequestParam(required = false) String lojaId, @RequestParam(required = false) String franquiaId){
        try{
            return ResponseEntity.ok().body(this.bannerService.getBanners(lojaId,franquiaId));
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/bulk")
    public ResponseEntity bulkDelete(@RequestParam List<String> ids){
        try{
            this.bulkBannerService.bulkDelete(ids);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity desassociarBanner(String bannerId, Boolean isMobile){
        try{
            this.bannerService.deletarBannerDaLoja(bannerId,isMobile);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }
}
