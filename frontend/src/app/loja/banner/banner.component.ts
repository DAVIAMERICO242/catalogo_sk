import { AfterViewInit, Component, OnInit } from '@angular/core';
import { BannerModel, BannerService } from '../../services/banner.service';
import { SharedModule } from '../../shared/shared.module';
import { LojaContextService } from '../loja-context.service';
import { CarouselPageEvent } from 'primeng/carousel';

@Component({
  selector: 'app-banner',
  imports: [SharedModule],
  templateUrl: './banner.component.html',
})
export class BannerComponent implements OnInit {
  banners:BannerModel.Banner[] = []
  bannersUrlDesktop:string[] = [];
  bannersUrlMobile:string[] = [];

  page = 0;

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

  changePage(val:CarouselPageEvent){
    this.page = 0;
  }
  

  setBannersUrls(){
    this.bannersUrlMobile = this.banners.flatMap(banner => banner.media.filter(e=>e.window===BannerModel.WindowContext.MOBILE).map(media => media.bannerUrl));
    this.bannersUrlDesktop = this.banners.flatMap(banner => banner.media.filter(e=>e.window===BannerModel.WindowContext.DESKTOP).map(media => media.bannerUrl));
  }
  
}
