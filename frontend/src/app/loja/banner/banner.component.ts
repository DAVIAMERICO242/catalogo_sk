import { Component, OnInit } from '@angular/core';
import { BannerModel, BannerService } from '../../services/banner.service';
import { SharedModule } from '../../shared/shared.module';
import { LojaContextService } from '../loja-context.service';

@Component({
  selector: 'app-banner',
  imports: [SharedModule],
  templateUrl: './banner.component.html',
})
export class BannerComponent implements OnInit {
  banners:BannerModel.Banner[] = []
  bannersUrlDesktop:string[] = [];
  bannersUrlMobile:string[] = [];

  constructor(private bannerService:BannerService,private lojaContext:LojaContextService){}
  ngOnInit(): void {
    this.lojaContext.loja$.subscribe((val)=>{
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
