import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { BannerModel, BannerService } from '../../../services/banner.service';
import { CatalogoContextService } from '../catalogo-context.service';

@Component({
  selector: 'app-catalogo-banner',
  imports: [SharedModule],
  templateUrl: './catalogo-banner.component.html',
  styleUrls:['./catalogo-banner.component.css']
})
export class CatalogoBannerComponent implements OnInit {
  banners:BannerModel.Banner[] = []
  bannersUrlDesktop:string[] = [];
  bannersUrlMobile:string[] = [];

  constructor(private bannerService:BannerService,private catalogoContext:CatalogoContextService){}
  ngOnInit(): void {
    this.catalogoContext.loja$.subscribe((val)=>{
      if(val?.systemId){
        this.bannerService.getBanners(undefined,val?.systemId).subscribe((banners)=>{
          this.banners = banners.sort((a,b)=>a.lojaInfo.index - b.lojaInfo.index);
          this.setBannersUrls();
        })
      }
    })
  }

  setBannersUrls(){
    this.bannersUrlMobile = this.banners.flatMap(banner => banner.media.filter(e=>e.window===BannerModel.WindowContext.MOBILE).map(media => media.bannerUrl));
    this.bannersUrlDesktop = this.banners.flatMap(banner => banner.media.filter(e=>e.window===BannerModel.WindowContext.DESKTOP).map(media => media.bannerUrl));
  }
  
}
